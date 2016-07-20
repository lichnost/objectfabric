
package org.objectfabric;

import org.objectfabric.CassandraLoop.Query;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

final class CassandraView extends ArrayView {

    private final byte[] _sha1;
    private final URI _uri;

    CassandraView(Location location, byte[] sha1, URI uri) {
        super(location);

        _sha1 = sha1;
        _uri = uri;
    }

    final byte[] sha1() {
        return _sha1;
    }
    
    final URI uri() {
        return _uri;
    }

    private final Cassandra db() {
        return (Cassandra) location();
    }

    @Override
    final void getKnown(URI uri) {
        long[] ticks = copy();

        if (ticks != null) {
            if (ticks.length != 0 || !location().isCache())
                uri.onKnown(this, ticks);
        } else
            list(uri, null);
    }

    @Override
    final void onKnown(URI uri, long[] ticks) {
        boolean load;

        synchronized (this) {
            load = isNull();
        }

        if (load)
            list(uri, ticks);
        else
            getUnknown(uri, ticks);
    }

    private final void list(final URI uri, final long[] compare) {
        if (Debug.PERSISTENCE_LOG)
            Log.write("Cassandra list blocks " + uri);

        if (Stats.ENABLED)
            Stats.Instance.BlockListCount.incrementAndGet();

        db().readers().add(new Query() {

            @Override
            void run(com.datastax.driver.core.Session session) {
                PreparedStatement st = session.prepare(Shared.LIST_BLOCKS);
                BoundStatement bst = new BoundStatement(st);

                long[] ticks = null;
                bst.setBytes("sha1", CassandraUtil.byteBuffer(_sha1));

                ResultSet rs = session.execute(bst);
                for (Row row : rs) {
                    long time = row.getLong("time");
                    byte[] peer = CassandraUtil.byteArray(row.getBytes("peer"));
                    ticks = Tick.add(ticks, Tick.get(Peer.get(new UID(peer)).index(), time));
                }

                if (ticks == null)
                    ticks = Tick.EMPTY;

                onLoad(uri, ticks, compare);

            }
        });
    }

    @Override
    void getBlock(final URI uri, final long tick) {
        if (!contains(tick))
            return;

        db().readers().add(new Query() {

            @Override
            void run(com.datastax.driver.core.Session session) {
                if (Debug.PERSISTENCE_LOG)
                    Log.write("Cassandra read block " + uri + " - " + Tick.toString(tick));

                if (Stats.ENABLED)
                    Stats.Instance.BlockReadCount.incrementAndGet();

                if (InFlight.starting(uri, tick)) {
                    PreparedStatement st = session.prepare(Shared.SELECT_BLOCK);
                    List<JVMBuff> list = new List<JVMBuff>();

                    BoundStatement bst = new BoundStatement(st);
                    bst.setBytes("sha1", CassandraUtil.byteBuffer(_sha1));
                    bst.setLong("time", Tick.time(tick));
                    bst.setBytes("peer", CassandraUtil.byteBuffer(Peer.get(Tick.peer(tick)).uid()));

                    ResultSet rs = session.execute(bst);
                    Row row = rs.one();
                    if (row != null) {
                        byte[] block = CassandraUtil.byteArray(row.getBytes("block"));

                        JVMBuff buff = JVMBuff.getWithPosition(0);
                        int offset = 0;

                        for (;;) {
                            int length = Math.min(buff.remaining(), block.length - offset);
                            buff.putImmutably(block, offset, length);
                            offset += length;
                            buff.limit(buff.position() + length);
                            list.add(buff);

                            if (offset == block.length)
                                break;

                            buff = JVMBuff.getWithPosition(Buff.getLargestUnsplitable());
                        }
                    }

                    if (list.size() > 0) {
                        JVMBuff[] buffs = new JVMBuff[list.size()];
                        list.copyToFixed(buffs);

                        if (Debug.ENABLED) {
                            for (int i = 0; i < buffs.length; i++) {
                                buffs[i].lock(buffs[i].limit());

                                if (Debug.THREADS)
                                    ThreadAssert.exchangeGive(buffs, buffs[i]);
                            }
                        }

                        Exception exception = uri.onBlock(CassandraView.this, tick, buffs, null, true, null, false, null);

                        if (Debug.THREADS)
                            ThreadAssert.exchangeTake(buffs);

                        if (exception != null) {
                            // TODO make sure exception is related to parsing
                            Log.write("Corrupted block " + exception.toString());
                            // TODO Make option or callback to clean corrupted
                            // file.delete();
                        }

                        for (int i = 0; i < buffs.length; i++)
                            buffs[i].recycle();
                    }
                }
            }
        });
    }

    @Override
    final void onBlock(URI uri, long tick, Buff[] buffs, long[] removals, boolean requested) {
        db().queue().enqueueBlock(uri, tick, buffs, removals, requested);
    }
}

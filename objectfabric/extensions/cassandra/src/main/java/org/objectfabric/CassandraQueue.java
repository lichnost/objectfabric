
package org.objectfabric;

import java.nio.ByteBuffer;

import org.objectfabric.CassandraLoop.Query;
import org.objectfabric.CloseCounter.Callback;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

final class CassandraQueue extends BlockQueue implements Runnable {

	private final Cassandra _location;

	CassandraQueue(Cassandra location) {
		_location = location;

		if (Debug.THREADS)
			ThreadAssert.exchangeGive(this, this);

		onStarted();
	}

	@Override
	void onClose(Callback callback) {
		Object key;

		if (Debug.ENABLED) {
			ThreadAssert.suspend(key = new Object());
			ThreadAssert.resume(this, false);
		}

		if (Debug.THREADS) {
			ThreadAssert.exchangeTake(this);
			ThreadAssert.removePrivate(this);
		}

		if (Debug.ENABLED)
			ThreadAssert.resume(key);

		super.onClose(callback);
	}

	@Override
	protected void enqueue() {
		Platform.get().execute(this);
	}

	@Override
	public void run() {
		if (onRunStarting()) {
			if (Debug.ENABLED)
				ThreadAssert.resume(this, false);

			runMessages(false);
			final List<Block> list = new List<Block>();
			int room = _location.writer().room();

			for (int i = 0; i < room; i++) {
				Block block = nextBlock();

				if (block == null)
					break;

				list.add(block);

				if (Debug.THREADS)
					for (int t = 0; t < block.Buffs.length; t++)
						ThreadAssert.exchangeGive(block, block.Buffs[t]);
			}

			if (list.size() > 0) {
				_location.writer().add(new Query() {

					@Override
					int statements() {
						return list.size();
					}

					@Override
					void run(com.datastax.driver.core.Session session) {
						for (int i = 0; i < list.size(); i++) {
							Block block = list.get(i);

							if (Debug.THREADS)
								ThreadAssert.exchangeTake(block);

							write(session, block.URI, block.Tick, block.Buffs,
									block.Removals);

							for (int b = 0; b < block.Buffs.length; b++)
								block.Buffs[b].recycle();
						}
					}

					@Override
					void ack() {
						for (int i = 0; i < list.size(); i++) {
							Block b = list.get(i);
							CassandraQueue.this.ack(b.URI, b.Tick, b.Removals);
						}

						// Might be room now
						requestRun();
					}
				});
			}

			if (Debug.ENABLED)
				ThreadAssert.suspend(this);

			onRunEnded(false);
		}
	}

	final void write(com.datastax.driver.core.Session session, URI uri,
			long tick, Buff[] buffs, long[] removals) {
		if (Debug.PERSISTENCE_LOG)
			Log.write("Cassandra write " + uri + " - " + Tick.toString(tick));

		if (Stats.ENABLED)
			Stats.Instance.BlockWriteCount.incrementAndGet();

		CassandraView view = (CassandraView) uri.getOrCreate(_location);

		ByteBuffer sha1 = CassandraUtil.byteBuffer(view.sha1());
		long time = Tick.time(tick);
		ByteBuffer peer = CassandraUtil.byteBuffer(Peer.get(Tick.peer(tick))
				.uid());

		int capacity = 0;

		for (int i = 0; i < buffs.length; i++)
			capacity += buffs[i].remaining();

		byte[] array = new byte[capacity];
		int offset = 0;

		for (int i = 0; i < buffs.length; i++) {
			int length = buffs[i].remaining();
			buffs[i].getImmutably(array, offset, length);
			offset += length;
		}

		 PreparedStatement st = session.prepare(Shared.REPLACE_DELETE_BLOCK);
		 BoundStatement bst = new BoundStatement(st);
		 bst.setBytes("sha1", sha1);
		 bst.setLong("time", time);
		 bst.setBytes("peer", peer);
		 session.execute(bst);
		
		 st = session.prepare(Shared.REPLACE_INSERT_BLOCK);
		 bst = new BoundStatement(st);
		 bst.setBytes("sha1", sha1);
		 bst.setLong("time", time);
		 bst.setBytes("peer", peer);
		 bst.setBytes("block", CassandraUtil.byteBuffer(array));
		 bst.setString("path", view.uri().path());
		 session.execute(bst);

		// PreparedStatement st = session.prepare(Shared.REPLACE_BLOCK);
		// BoundStatement bst = new BoundStatement(st);
		// bst.setBytes("sha1", sha1);
		// bst.setLong("time", time);
		// bst.setBytes("peer", peer);
		// bst.setBytes("block", CassandraUtil.byteBuffer(array));
		// session.execute(bst);

		if (removals != null)
			for (int i = 0; i < removals.length; i++)
				if (!Tick.isNull(removals[i]))
					delete(session, view, removals[i]);
	}

	private final void delete(com.datastax.driver.core.Session session,
			CassandraView view, long tick) {
		PreparedStatement st = session.prepare(Shared.DELETE_BLOCK);

		ByteBuffer sha1 = CassandraUtil.byteBuffer(view.sha1());
		long time = Tick.time(tick);
		ByteBuffer peer = CassandraUtil.byteBuffer(Peer.get(Tick.peer(tick))
				.uid());

		BoundStatement bst = new BoundStatement(st);
		bst.setBytes("sha1", sha1);
		bst.setLong("time", time);
		bst.setBytes("peer", peer);
		session.execute(bst);
	}

	private final void ack(URI uri, long tick, long[] removals) {
		Object key;

		if (Debug.THREADS)
			ThreadAssert.suspend(key = new Object());

		CassandraView view = (CassandraView) uri.getOrCreate(_location);
		uri.onAck(view, tick);
		view.add(tick, removals);

		if (Debug.THREADS)
			ThreadAssert.resume(key);
	}
}

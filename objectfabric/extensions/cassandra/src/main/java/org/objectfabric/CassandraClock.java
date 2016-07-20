
package org.objectfabric;

import java.nio.ByteBuffer;

import org.objectfabric.Actor.Message;
import org.objectfabric.CassandraLoop.Query;
import org.objectfabric.Resource.NewBlock;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

final class CassandraClock extends Clock {

	private final Cassandra _location;

	CassandraClock(Watcher watcher, Cassandra location) {
		super(watcher);

		_location = location;
	}

	@Override
	void writing(Resources resources) {
		final long[][] loaded = new long[resources.size()][];

		for (int i = 0; i < resources.size(); i++)
			loaded[i] = Platform.get().clone(resources.get(i).loaded());

		_location.writer().add(new Query() {

			@Override
			void run(com.datastax.driver.core.Session session) {
				Peer peer = null;
				long time = 0, object = 0;
				boolean foundOne = false;

				ResultSet rs = session.execute(Shared.SELECT_CLOCKS);
				for (Row row : rs) {
					peer = Peer.get(new UID(CassandraUtil.byteArray(row
							.getBytes("peer"))));
					time = row.getLong("time");
					object = row.getLong("object");

					long tick = Tick.get(peer.index(), time);

					if (upToDate(loaded, tick)) {
						foundOne = true;
						break;
					}
				}

				if (!foundOne) {
					peer = Peer.get(new UID(Platform.get().newUID()));
					time = Clock.time(0, false);
					object = 0;
				}

				_location.writer().walks().put(peer, peer);

				final Peer peer_ = peer;
				final long time_ = time;
				final long object_ = object;

				watcher().actor().addAndRun(new Message() {

					@Override
					void run() {
						watcher().clock().init(peer_, time(time_, true),
								object_);
					}
				});
			}
		});
	}

	private final boolean upToDate(long[][] loaded, long tick) {
		for (int i = 0; i < loaded.length; i++)
			if (!Tick.happenedBefore(tick, loaded[i]))
				return false;

		return true;
	}

	@Override
	void commit() {
		if (Debug.ENABLED)
			Debug.assertion((peer() == null) == (blocks().size() == 0));

		if (blocks().size() > 0) {
			final NewBlock[] blocks = new NewBlock[blocks().size()];
			final Buff[][] duplicates = new Buff[blocks.length][];

			for (int i = blocks.length - 1; i >= 0; i--) {
				blocks[i] = blocks().removeLast();
				duplicates[i] = new Buff[blocks[i].Buffs.length];

				for (int d = 0; d < blocks[i].Buffs.length; d++) {
					duplicates[i][d] = blocks[i].Buffs[d].duplicate();

					if (Debug.THREADS)
						ThreadAssert.exchangeGive(duplicates, duplicates[i][d]);
				}
			}

			final Peer peer = peer();
			final long time = time();
			final long object = object();
			init(null, 0, 0);

			_location.writer().add(new Query() {

				@Override
				void run(com.datastax.driver.core.Session session) {
					if (Debug.THREADS)
						ThreadAssert.exchangeTake(duplicates);

					ByteBuffer peerBytes = CassandraUtil.byteBuffer(peer.uid());

					PreparedStatement st = session
							.prepare(Shared.REPLACE_DELETE_CLOCK);
					BoundStatement bst = new BoundStatement(st);
					bst.setBytes("peer", peerBytes);
					session.execute(bst);

					st = session.prepare(Shared.REPLACE_INSERT_CLOCK);
					bst = new BoundStatement(st);
					bst.setBytes("peer", peerBytes);
					bst.setLong("time", time);
					bst.setLong("object", object);
					session.execute(bst);

//					PreparedStatement st = session
//							.prepare(Shared.REPLACE_CLOCK).enableTracing();
//					BoundStatement bst = new BoundStatement(st);
//					bst.setBytes("peer", peerBytes);
//					bst.setLong("time", time);
//					bst.setLong("object", object);
//					session.execute(bst);

					for (int i = 0; i < blocks.length; i++) {
						_location.queue().write(session,
								blocks[i].Resource.uri(), blocks[i].Tick,
								duplicates[i], blocks[i].Removals);

						if (Debug.THREADS)
							for (int d = 0; d < duplicates[i].length; d++)
								ThreadAssert.exchangeGive(watcher().actor(),
										duplicates[i][d]);
					}

					_location.writer().walks().remove(peer);
				}

				@Override
				void ack() {
					watcher().actor().addAndRun(new Message() {

						@Override
						void run() {
							for (int i = 0; i < blocks.length; i++) {
								CassandraView view = (CassandraView) blocks[i].Resource
										.uri().getOrCreate(_location);
								view.add(blocks[i].Tick, blocks[i].Removals);

								publish(blocks[i].Resource, blocks[i].Tick,
										duplicates[i], blocks[i].Removals,
										_location);

								for (int d = 0; d < duplicates[i].length; d++)
									duplicates[i][d].recycle();

								blocks[i].Resource.ack(_location,
										blocks[i].Tick);
							}
						}
					});
				}
			});
		}
	}
}

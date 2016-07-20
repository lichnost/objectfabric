

package org.objectfabric;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

final class CassandraLoop {

	private static final int MAX_BATCH = 100; // TODO tune

	static abstract class Query {

		int statements() {
			return 1;
		}

		abstract void run(com.datastax.driver.core.Session session);

		void ack() {
		}
	}

	private com.datastax.driver.core.Session _session;

	private final Run[] _runs;

	private final LinkedBlockingQueue<Query> _queue = new LinkedBlockingQueue<Query>();

	private final AtomicInteger _ongoing;

	private final HashMap<Peer, Peer> _walks;

	private volatile boolean _running = true;

	CassandraLoop(com.datastax.driver.core.Session session, int threads,
			boolean count) {
		_session = session;
		_runs = new Run[threads];

		for (int i = 0; i < threads; i++) {
			_runs[i] = new Run();
			_runs[i].start();
		}

		if (count) {
			_ongoing = new AtomicInteger();
			_walks = new HashMap<Peer, Peer>();
		} else {
			_ongoing = null;
			_walks = null;
		}
	}

	final void close() {
		_running = false;

		for (int i = 0; i < _runs.length; i++) {
			_runs[i].interrupt();

			try {
				_runs[i].join();
			} catch (InterruptedException e) {
			}
		}
	}

	final int room() {
		return MAX_BATCH - _ongoing.get();
	}

	final void add(Query query) {
		_queue.offer(query);

		if (_ongoing != null)
			_ongoing.addAndGet(query.statements());
	}

	final HashMap<Peer, Peer> walks() {
		return _walks;
	}

	private final class Run extends Thread {

		Run() {
			setName("CassandraQueue");
			setDaemon(true);
		}

		@Override
		public void run() {
			List<Query> toAck = new List<Query>();

			try {

				while (_running) {
					Query query = _queue.take();

					if (_ongoing == null)
						query.run(_session);
					else {
						if (_walks.size() == 0) {
							/*
							 * Immediate because clock adds reads at beginning
							 * of transaction, which can lead to deadlock if
							 * done from multiple processes.
							 */
							// _session.execute("BEGIN BATCH");

							if (Debug.ENABLED)
								Debug.assertion(toAck.size() == 0);
						}
						while (query != null) {
							query.run(_session);
							toAck.add(query);
							query = _queue.poll();
						}

						if (_walks.size() == 0) {
							// _session.execute("APPLY BATCH");

							int count = 0;

							for (int i = 0; i < toAck.size(); i++)
								count += toAck.get(i).statements();

							_ongoing.addAndGet(-count);

							for (int i = 0; i < toAck.size(); i++)
								toAck.get(i).ack();

							toAck.clear();
						}
					}
				}
			} catch (Exception e) {
				if (!(e instanceof InterruptedException))
					Log.write(e);
			}
		}
	}

}

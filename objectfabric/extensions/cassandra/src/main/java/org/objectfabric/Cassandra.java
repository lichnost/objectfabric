package org.objectfabric;

import java.io.Closeable;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

public class Cassandra extends Origin implements URIHandler, Closeable {

	static {
		JVMPlatform.loadClass();
	}

	private final String _host;

	private int _port = 0;

	private final String _username, _password;

	private final String _keyspace;

	private Cluster _cluster;

	private com.datastax.driver.core.Session _session;

	private final CassandraQueue _queue;

	private final CassandraLoop _readers;

	private final CassandraLoop _writer;

	public Cassandra(String host, String keyspace, boolean cache) {
		this(host, null, keyspace, null, null, cache, 1);
	}

	public Cassandra(String host, Integer port, String keyspace, boolean cache) {
		this(host, port, null, null, keyspace, cache, 1);
	}

	public Cassandra(String host, Integer port, String username,
			String password, String keyspace, boolean cache, int readers) {
		super(cache);

		if (Debug.PERSISTENCE_LOG)
			Log.write("Cassandra opened");

		_host = host;
		if (port != null) {
			_port = port;
		}
		_username = username;
		_password = password;
		_keyspace = keyspace;
		sessionOpen();

		_queue = new CassandraQueue(this);
		_readers = new CassandraLoop(_session, readers, false);
		_writer = new CassandraLoop(_session, 1, true);
	}

	public final String host() {
		return _host;
	}

	public final int port() {
		return _port;
	}

	public final String keyspace() {
		return _keyspace;
	}

	public final String username() {
		return _username;
	}

	public final String password() {
		return _password;
	}

	public void close() {
		_queue.requestClose(null);
		_readers.close();
		_writer.close();
		sesssionClose();
	}

	// TODO?
	// public void clear() {
	// for (WeakReference<Remote> ref : ClientURIHandler.remotes().values()) {
	// Remote remote = ref.get();
	//
	// if (remote != null)
	// for (URI uri : remote.uris().values())
	// ((ArrayView) uri.getOrCreate(this)).reset();
	// }
	// }

	final CassandraQueue queue() {
		return _queue;
	}

	final CassandraLoop readers() {
		return _readers;
	}

	final CassandraLoop writer() {
		return _writer;
	}

	@Override
	View newView(URI uri) {
		SHA1Digest digest = new SHA1Digest();
		uri.origin().sha1(digest);
		digest.update(uri.path());
		byte[] sha1 = new byte[SHA1Digest.LENGTH];
		digest.doFinal(sha1, 0);
		return new CassandraView(this, sha1, uri);
	}

	@Override
	Clock newClock(Watcher watcher) {
		return new CassandraClock(watcher, this);
	}

	@Override
	public URI handle(Address address, String path) {
		return getURI(path);
	}

	@Override
	public String toString() {
		return "Cassandra";
	}

	private void sessionOpen() {
		if (_session == null) {
			if (Debug.PERSISTENCE_LOG)
				Log.write("Cassandra session create");
			try {
				Builder builder = Cluster.builder().addContactPoint(host());
				if (port() != 0) {
					builder.withPort(port());
				}
				if (username() != null && password() != null) {
					builder.withCredentials(username(), password());
				}
				_cluster = builder.build();
				_session = _cluster.connect();

				synchronized (CassandraLoop.class) {// TODO not for distributed
					if (_cluster.getMetadata().getKeyspace(keyspace()) == null) {
						_session.execute(Shared.CREATE_KEYSPACE.replaceFirst(
								":keyspace", keyspace()));
						_session.execute(Shared.USE_KEYSPACE.replaceFirst(
								":keyspace", keyspace()));
						_session.execute(Shared.INIT_BLOCKS);
						_session.execute(Shared.INIT_CLOCKS);
					}
				}
				_session.execute(Shared.USE_KEYSPACE.replaceFirst(":keyspace",
						keyspace()));
			} catch (IllegalArgumentException e) {
				Log.write("Cassandra can not find host", e);
				throw e;
			}
		}
	}

	private void sesssionClose() {
		_session.close();
		_cluster.close();
	}
	
}

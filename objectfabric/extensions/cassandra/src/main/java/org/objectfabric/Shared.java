
package org.objectfabric;

/**
 * Shared by all Cassandra implementations.
 */
final class Shared {

    static final String BLOCKS = "blocks", CLOCKS = "clocks";

    static final String CREATE_KEYSPACE = "CREATE KEYSPACE :keyspace  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};";

    static final String USE_KEYSPACE = "USE :keyspace";

    static final String INIT_BLOCKS = "CREATE TABLE " + BLOCKS + " (sha1 BLOB, time BIGINT, peer BLOB, block BLOB, path VARCHAR, PRIMARY KEY (sha1, time, peer))";

    static final String INIT_CLOCKS = "CREATE TABLE " + CLOCKS + " (peer BLOB PRIMARY KEY, time BIGINT, object BIGINT)";

    static final String LIST_BLOCKS = "SELECT time, peer FROM " + BLOCKS + " WHERE sha1 = :sha1";

    static final String SELECT_BLOCK = "SELECT block FROM " + BLOCKS + " WHERE sha1 = :sha1 AND time = :time AND peer = :peer";

    // static final String REPLACE_BLOCK = "BEGIN BATCH DELETE FROM " + BLOCKS +
    // " WHERE sha1 = :sha1 AND time = :time AND peer = :peer; INSERT INTO " + BLOCKS +
    // "(sha1, time, peer, block) VALUES (:sha1, :time, :peer, :block); APPLY BATCH;";
    static final String REPLACE_DELETE_BLOCK = "DELETE FROM " + BLOCKS + " WHERE sha1 = :sha1 AND time = :time AND peer = :peer";

    static final String REPLACE_INSERT_BLOCK = "INSERT INTO " + BLOCKS + "(sha1, time, peer, block, path) VALUES (:sha1, :time, :peer, :block, :path)";

    static final String DELETE_BLOCK = "DELETE FROM " + BLOCKS + " WHERE sha1 = :sha1 AND time = :time AND peer = :peer";

    static final String SELECT_CLOCKS = "SELECT peer, time, object FROM " + CLOCKS;

    // static final String REPLACE_CLOCK = "BEGIN BATCH DELETE FROM " + CLOCKS +
    // " WHERE peer = :peer; INSERT INTO " + CLOCKS +
    // " (peer, time, object) VALUES (:peer, :time, :object); APPLY BATCH;";
    static final String REPLACE_DELETE_CLOCK = "DELETE FROM " + CLOCKS + " WHERE peer = :peer";

    static final String REPLACE_INSERT_CLOCK = "INSERT INTO " + CLOCKS + " (peer, time, object) VALUES (:peer, :time, :object)";
}

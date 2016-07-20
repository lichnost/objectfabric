package org.objectfabric;
//package com.objectsync;
//
//import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
//import org.junit.Test;
//
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.Cluster.Builder;
//import com.datastax.driver.core.ResultSet;
//
//public class CassandraDatabaseTest extends TestsHelper {
//
//	@Test
//	public void testOpenCloseSession() throws Exception {
//		Log.write("Cassandra starting");
//		EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000);
//		Log.write("Cassandra started");
//
//		Builder builder = Cluster.builder().addContactPoint(
//				EmbeddedCassandraServerHelper.getHost());
//		builder.withPort(EmbeddedCassandraServerHelper.getNativeTransportPort());
//
//		Cluster cluster = builder.build();
//		com.datastax.driver.core.Session session = cluster.connect();
//
////		TODO High CPU load
////		session.close();
////		cluster.close();
//
//		Log.write("Cassandra stopping");
//		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
//		Log.write("Cassandra stopped");
//	}
//
//	@Test
//	public void testCreateTableWriteRead() throws Exception {
//		Log.write("Cassandra starting");
//		EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000);
//		Log.write("Cassandra started");
//
//		Builder builder = Cluster.builder().addContactPoint(
//				EmbeddedCassandraServerHelper.getHost());
//		builder.withPort(EmbeddedCassandraServerHelper.getNativeTransportPort());
//
//		Cluster cluster = builder.build();
//		com.datastax.driver.core.Session session = cluster.connect();
//
//		session.execute("CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3}");
//		session.execute("USE test");
//		
//		session.execute("CREATE TABLE test (value INT PRIMARY KEY)");
//		session.execute("INSERT INTO test (value) values (1)");
//		session.execute("INSERT INTO test (value) values (2)");
//		session.execute("INSERT INTO test (value) values (3)");
//		session.execute("INSERT INTO test (value) values (4)");
//
//		ResultSet rs = session.execute("SELECT value FROM test");
//		rs.all();
//		
////		TODO High CPU load
////		session.close();
////		cluster.close();
//
//		Log.write("Cassandra stopping");
//		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
//		Log.write("Cassandra stopped");
//	}
//
//}

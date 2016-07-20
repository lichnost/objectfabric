
package org.objectfabric;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectfabric.Cassandra;

public class CassandraTest extends TestsHelper {


    @Test
    public void test()  throws Exception  {
        
        Log.write("Cassandra starting");
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000);
        Log.write("Cassandra started");

        for (int i = 0; i < 1; i++) {
            Workspace workspace = new JVMWorkspace();
            Cassandra db = new Cassandra(EmbeddedCassandraServerHelper.getHost(), EmbeddedCassandraServerHelper.getNativeTransportPort(), "testkeyspace", false);
            workspace.addURIHandler(db);

            for (int j = 0; j < 1000; j++)
                workspace.open("any:///test").set("data" + j);

            workspace.open("any:///test1").set("test1");
            workspace.open("any:///test2").set("test2");
            workspace.open("any:///test3");
            workspace.open("any:///test1").set("test4");
            workspace.open("any:///test2").set("test5");
            workspace.open("any:///test3");
            workspace.open("any:///test4").set("test7");
            
            workspace.close();

            // TODO High CPU load on close Cassandra session
            // db.close();

            workspace = new JVMWorkspace();
            // db = new Cassandra(EmbeddedCassandraServerHelper.getHost(),
            // EmbeddedCassandraServerHelper.getNativeTransportPort(),
            // "testkeyspace", false);
            workspace.addURIHandler(db);
            Resource test = workspace.open("any:///test");
            Assert.assertEquals("data999", test.get());
            test.set("update");
            workspace.close();
            // db.close();

            workspace = new JVMWorkspace();
            // db = new Cassandra(EmbeddedCassandraServerHelper.getHost(),
            // EmbeddedCassandraServerHelper.getNativeTransportPort(),
            // "testkeyspace", false);
            workspace.addURIHandler(db);
            String value = (String) workspace.open("any:///test").get();
            Assert.assertEquals("update", value);
            workspace.close();
            // db.close();

            Log.write("Cassandra stopping");
            EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
            Log.write("Cassandra stopped");
        }
    }

}

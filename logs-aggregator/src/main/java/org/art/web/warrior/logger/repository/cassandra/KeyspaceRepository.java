package org.art.web.warrior.logger.repository.cassandra;

import org.art.web.warrior.logger.config.cassandra.CassandraConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KeyspaceRepository {

    private CassandraConnector connector;

    @Autowired
    public KeyspaceRepository(CassandraConnector connector) {
        this.connector = connector;
    }

    public void createKeyspace(String keyspaceName, String replicationStrategy, int numberOfReplicas) {
        StringBuilder sb = new StringBuilder()
            .append("CREATE KEYSPACE IF NOT EXISTS ")
            .append(keyspaceName)
            .append(" WITH replication = {")
            .append("'class':'")
            .append(replicationStrategy)
            .append("','replication_factor':")
            .append(numberOfReplicas).append("};");
        String query = sb.toString();
        connector.getSession().execute(query);
    }

    public void useKeyspace(String keyspace) {
        connector.getSession().execute("USE " + keyspace);
    }

    public void deleteKeyspace(String keyspaceName) {
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ")
            .append(keyspaceName);
        String query = sb.toString();
        connector.getSession().execute(query);
    }
}

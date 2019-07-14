package org.art.web.warrior.logger.config.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class CassandraConnector {

    @Value("${cassandra.port}")
    private String port;

    @Value("${cassandra.host}")
    private String host;

    private Cluster cluster;

    private Session session;

    public CassandraConnector() {
        connect();
    }

    private void connect() {
        Cluster.Builder builder = Cluster.builder()
            .addContactPoint(host)
            .withoutJMXReporting();
        if (port != null) {
            builder.withPort(NumberUtils.toInt(port));
        }
        cluster = builder.build();
        Metadata metadata = cluster.getMetadata();
        log.info("Cluster name: {}", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            log.info("Data center: {}. Host: {}. Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void shutdown() {
        session.close();
        cluster.close();
    }
}

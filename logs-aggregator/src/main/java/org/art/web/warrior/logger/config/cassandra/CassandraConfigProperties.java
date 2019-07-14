package org.art.web.warrior.logger.config.cassandra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.art.web.warrior.logger.ServiceCommonConstants.TASK_MESSAGE_KEYSPACE_NAME;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "cassandra")
public class CassandraConfigProperties {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_PORT = "9042";
    private static final String DEFAULT_REPLICATION_STRATEGY = "SimpleStrategy";
    private static final String DEFAULT_REPLICAS_NUMBER = "1";

    private String host = DEFAULT_HOST;

    private String port = DEFAULT_PORT;

    private String keyspace = TASK_MESSAGE_KEYSPACE_NAME;

    private String replicationStrategy = DEFAULT_REPLICATION_STRATEGY;

    private String replicas = DEFAULT_REPLICAS_NUMBER;
}

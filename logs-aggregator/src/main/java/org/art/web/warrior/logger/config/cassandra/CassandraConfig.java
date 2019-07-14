package org.art.web.warrior.logger.config.cassandra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {

    @Bean
    public CassandraConnector cassandraConnector() {
        return new CassandraConnector();
    }
}

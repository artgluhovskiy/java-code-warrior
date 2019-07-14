package org.art.web.warrior.logger;

import org.art.web.warrior.logger.config.cassandra.CassandraConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@EnableConfigurationProperties(CassandraConfigProperties.class)
public class LogsAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogsAggregatorApplication.class, args);
    }
}
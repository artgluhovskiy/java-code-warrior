package org.art.web.warrior.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class LogsAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogsAggregatorApplication.class, args);
    }
}
package org.art.web.warrior.tasking.config.kafka.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Consumer<K, V> implements Runnable {

    private final List<String> topicNames;

    private final Properties properties;

    public Consumer(List<String> topicNames, Properties properties) {
        this.topicNames = topicNames;
        this.properties = properties;
    }

    @Override
    public void run() {
        log.info("Consumer is running!");
        try (KafkaConsumer<K, V> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(topicNames);
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.of(10, ChronoUnit.SECONDS));
                for (ConsumerRecord<K, V> record : records) {
                    log.info("Consumer. Message received: topic - {}, partition - {}, value - {}", record.topic(), record.partition(), record.value());
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while consuming Kafka message!", e);
        }
    }
}

package org.art.web.warrior.tasking.config.kafka.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class Producer<K, V> implements Runnable {

    private final String topicName;

    private final Properties properties;

    private final Supplier<K> keySupplier;

    private final Supplier<V> messageSupplier;

    public Producer(String topicName, Properties properties, Supplier<K> keySupplier, Supplier<V> messageSupplier) {
        this.topicName = topicName;
        this.properties = properties;
        this.keySupplier = keySupplier;
        this.messageSupplier = messageSupplier;
    }

    @Override
    public void run() {
        log.info("Producer is running!");
        try (KafkaProducer<K, V> producer = new KafkaProducer<>(properties)) {
            while (!Thread.currentThread().isInterrupted()) {
                K key = keySupplier.get();
                V value = messageSupplier.get();
                log.info("Producer. Message sent: topic - {}, key - {}, value - {}", topicName, key, value);
                producer.send(new ProducerRecord<>(topicName, key, value));
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            log.error("Exception occurred while producing Kafka message!", e);
        }
    }
}

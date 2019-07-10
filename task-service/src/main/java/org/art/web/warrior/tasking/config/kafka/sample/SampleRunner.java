package org.art.web.warrior.tasking.config.kafka.sample;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SampleRunner {

    public static void main(String[] args) throws InterruptedException {
        String consumerGroup = "test-group-1";
        String topic1 = "test-topic-1";

        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("group.id", consumerGroup);

        Consumer<String, String> consumer1 = new Consumer<>(Collections.singletonList(topic1), consumerProps);

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer1 = new Producer<>(topic1, producerProps, () -> String.valueOf(new Random().nextInt(4)), () -> "Producer 1 Message");

        Thread c1 = new Thread(consumer1);
        c1.start();
        Thread c2 = new Thread(consumer1);
        c2.start();
        Thread p1 = new Thread(producer1);
        p1.start();

        TimeUnit.MINUTES.sleep(2);

        c1.interrupt();
        c2.interrupt();
        p1.interrupt();
    }
}

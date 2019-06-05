package com.example;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerApplication {

    private static final String DEFAULT_TOPIC = "transactions";

    private static final String SYSTEM_PROPERTY_TOPIC = "topic";

    public static void main(String[] args) {
        final String topic = System.getProperty(SYSTEM_PROPERTY_TOPIC, DEFAULT_TOPIC);
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", System.getProperty("bootstrap.servers", "localhost:9092"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-payments");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                System.getProperty(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
        );
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        final KafkaConsumer<String, Payment> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topic));
        System.out.println("Subscribed to topic " + topic);
        while (true) {
            final ConsumerRecords<String, Payment> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, Payment> record : records) {
                final String key = record.key();
                final Payment value = record.value();
                System.out.printf("key = %s, value = %s%n", key, value);
            }
        }
    }
}

package com.example;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.Properties;

public class ProducerApplication {

    private static final String TOPIC = "transactions";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        KafkaProducer<String, Payment> paymentProducer = new KafkaProducer<>(props);
        final String orderId = "1";
        final Payment payment1 = new Payment(orderId, 1.00);
        final ProducerRecord<String, Payment> record1 = new ProducerRecord<>(TOPIC, payment1.getId().toString(), payment1);

        try {
            paymentProducer.send(record1);
            System.out.println("Message successfully sent.");
        } catch(SerializationException e) {
            e.printStackTrace();
        }
        // When you're finished producing records, you can flush the producer to ensure it has all been written to Kafka and
        // then close the producer to free its resources.
        finally {
            paymentProducer.flush();
            paymentProducer.close();
        }
    }
}

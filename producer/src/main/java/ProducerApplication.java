
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


import java.util.Properties;

public class ProducerApplication {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        String key = "key1";
        String userSchema = "{\"type\":\"record\"," +
                "\"name\":\"myrecord\"," +
                "\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}";
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(userSchema);

        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("f1", "value1");

        KafkaProducer<Object, Object> producer = new KafkaProducer(props);
        ProducerRecord<Object, Object> record = new ProducerRecord<>("test", key, avroRecord);
        try {
            producer.send(record);
            System.out.println("Message successfully sent.");
        } catch(SerializationException e) {
            e.printStackTrace();
        }
        // When you're finished producing records, you can flush the producer to ensure it has all been written to Kafka and
        // then close the producer to free its resources.
        finally {
            producer.flush();
            producer.close();
        }

        producer.close();
    }
}

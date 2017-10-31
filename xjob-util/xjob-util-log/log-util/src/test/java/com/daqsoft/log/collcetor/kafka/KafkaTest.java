package com.daqsoft.log.collcetor.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.After;
import org.junit.Before;

import java.util.Properties;

/**
 * Created by ShawnShoper on 2016/11/9.
 */
public class KafkaTest {
    KafkaConsumer<String, String> consumer;
    KafkaProducer<String, String> producer;

    @Before
    public void init() {
        {
//            Properties props = new Properties();
//            props.put("bootstrap.servers", "192.168.2.114:9092");
//            props.put("group.id", "test");
//            props.put("auto.offset.reset", "earliest");
//            props.put("enable.auto.commit", "true");
//            props.put("auto.commit.interval.ms", "1000");
//            props.put("session.timeout.ms", "30000");
//            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//            consumer = new KafkaConsumer<>(props);
            Properties props1 = new Properties();
            props1.put("bootstrap.servers", "192.168.2.115:9092");
            props1.put("zookeeper.connect", "192.168.2.115:2181");
            props1.put("group.id", "test");
            props1.put("auto.offset.reset", "earliest");
            props1.put("enable.auto.commit", "true");
            props1.put("auto.commit.interval.ms", "1000");
            props1.put("session.timeout.ms", "30000");
            props1.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props1.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer<>(props1);
        }
        {
            Properties props = new Properties();
            props.put("bootstrap.servers", "192.168.2.115:9092");
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(props);
        }
    }

    @After
    public void destory() {
        consumer.close();
        producer.close();
    }
}

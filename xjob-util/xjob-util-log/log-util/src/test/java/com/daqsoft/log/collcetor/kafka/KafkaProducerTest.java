package com.daqsoft.log.collcetor.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

/**
 * Created by ShawnShoper on 2016/11/9.
 */
public class KafkaProducerTest extends KafkaTest {
    @Test
    public void sendMessage() {
        for(int i = 0 ;i<3;i++) {
            String topic = "test21";
            String message = "world-by java code";
            ProducerRecord<String, String> shawnshoperTest = new ProducerRecord<>(topic, message);
            producer.send(shawnshoperTest);
        }
    }
}

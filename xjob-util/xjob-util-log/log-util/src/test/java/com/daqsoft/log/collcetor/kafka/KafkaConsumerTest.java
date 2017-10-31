package com.daqsoft.log.collcetor.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by ShawnShoper on 2016/11/9.
 */
public class KafkaConsumerTest extends KafkaTest {

    @Test
    public void showAllTopic() {
        Map<String, List<PartitionInfo>> topics = consumer.listTopics();
        int i = 0;
        for (String key : topics.keySet()) {
            System.out.println("topic " + i + "\t" + key);
//            List<PartitionInfo> partitionInfos = topics.get(key);
//            partitionInfos.forEach(e -> System.out.println("\t" + e));
            i++;
        }
    }

    @Test
    public void subscribe_from_begin() {
        consumer.subscribe(Arrays.asList("test"));
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            if (!flag) {
                TopicPartition topicPartition = new TopicPartition("test", consumer.partitionsFor("test").get(0).partition());
                consumer.seekToBeginning(Arrays.asList(topicPartition));
                flag = true;
                continue;
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value() + "\n");
        }
    }

    /**
     * 订阅服务,支持多组服务进行订阅,以及正则匹配分组
     */
    @Test
    public void subscribe() {
        consumer.subscribe(Arrays.asList("sys-log"));
        for (int i = 0; i < 3; i++) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                consumer.commitSync();
            }
        }
//        subscribeTopic("shoper1");
//        subscribeTopics(Arrays.asList("shoper1"));
//        subscribeTopicsByPattern("/^test\\w+shoper$/");
    }

    public void subscribeTopic(String topic) {
        consumer.subscribe(Pattern.compile("/^" + topic + "$/"), new NoOpConsumerRebalanceListener());
        for (int i = 0; i < 3; i++) {
            Iterable<ConsumerRecord<String, String>> records = consumer.poll(100);
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> next = iterator.next();
                System.out.println(next.topic() + ":" + next.value());
            }
        }
    }

    public void subscribeTopics(Collection<String> topics) {
        consumer.subscribe(topics);
//        TopicPartition topicPartition = new TopicPartition("shoper1",0);
//        consumer.seekToBeginning(Arrays.asList(topicPartition));
        for (int i = 0; i < 3; i++) {
            Iterable<ConsumerRecord<String, String>> records = consumer.poll(1000);
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> next = iterator.next();
                System.out.println(next.topic() + ":" + next.value());
            }
        }
    }
    @Test
    public void print(){
        subscribeTopics(Arrays.asList("test"));
    }
    public void subscribeTopicsByPattern(String pattern) {
        consumer.subscribe(Pattern.compile(pattern), new NoOpConsumerRebalanceListener());
        for (int i = 0; i < 3; i++) {
            Iterable<ConsumerRecord<String, String>> records = consumer.poll(1000);
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> next = iterator.next();
                System.out.println(next.topic() + ":" + next.value());
            }
        }
    }

}

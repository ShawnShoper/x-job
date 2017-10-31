package com.daqsoft.log.collcetor.kafka;

import kafka.admin.TopicCommand;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Created by ShawnShoper on 2016/11/10.
 */
public class TopicTest extends KafkaTest {
    @Test
    public void deleteTopic() {
        ZkUtils zkUtils = new ZkUtils(new ZkClient("localhost:2181"), new ZkConnection("localhost:2181"), false);
        String topic = "shoper1";
        String[] options = new String[]{"--delete", "--zookeeper", "localhost:2181", "--topic", topic};//, "--replication-factor", "2", "--partitions", "1"};
        TopicCommand.TopicCommandOptions topicCommandOptions = new TopicCommand.TopicCommandOptions(options);
        TopicCommand.deleteTopic(zkUtils, topicCommandOptions);
    }

    @Test
    public void createTopic() throws IOException {
        Process exec = Runtime.getRuntime().exec("/Users/ShawnShoper/software/kafka_2.10-0.10.1.0/bin/./kafka-topics.sh" +
                " --create --topic shoper1 " +
                "--zookeeper localhost:2181 " +
                "--partitions 1 --replication-factor 1");
        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String msg;
        while(Objects.nonNull(msg = reader.readLine())){
            System.out.println(msg);
        }
        reader.close();
    }
}

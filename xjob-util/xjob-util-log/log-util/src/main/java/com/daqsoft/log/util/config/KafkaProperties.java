package com.daqsoft.log.util.config;

/**
 * Created by ShawnShoper on 2017/5/23.
 */
public class KafkaProperties {
    //kafka配置服务器地址
    private String kafkaServer;
    //kafka 服务的key
    private String kafkaKey;
    //kafka 服务的cert
    private String kafkaCert;
    //kafka备份目录
    private String kafkaBackDir;

    public String getKafkaCert() {
        return kafkaCert;
    }

    public void setKafkaCert(String kafkaCert) {
        this.kafkaCert = kafkaCert;
    }

    public String getKafkaBackDir() {
        return kafkaBackDir;
    }

    public void setKafkaBackDir(String kafkaBackDir) {
        this.kafkaBackDir = kafkaBackDir;
    }

    public String getKafkaKey() {
        return kafkaKey;
    }

    public void setKafkaKey(String kafkaKey) {
        this.kafkaKey = kafkaKey;
    }

    public String getKafkaServer() {
        return kafkaServer;
    }

    public void setKafkaServer(String kafkaServer) {
        this.kafkaServer = kafkaServer;
    }
}

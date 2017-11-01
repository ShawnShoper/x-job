package org.shoper.log.util.config;


import org.shoper.log.core.config.Target;
import org.shoper.log.util.constans.LogLevel;

/**
 * Created by ShawnShoper on 2016/12/19 0019.
 * 日志配置类
 */

public class LogProperties {
    //输出终端
    private Target[] targets;
    //系统的host
    private String host;
    //系统的port
    private int port;
    //系统名
    private String application;
    //kafka配置
    private KafkaProperties kafka;
    //日志级别
    private LogLevel logLevel = LogLevel.Debug;
    //File配置
    private FileProperties fileProperties;
    //日志输出模板
    private String partten;

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public KafkaProperties getKafka() {
        return kafka;
    }

    public void setKafka(KafkaProperties kafka) {
        this.kafka = kafka;
    }

    public FileProperties getFileProperties() {
        return fileProperties;
    }

    public String getPartten() {
        return partten;
    }

    public void setPartten(String partten) {
        this.partten = partten;
    }

    public void setFileProperties(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    public Target[] getTargets() {
        return targets;
    }

    public void setTargets(Target[] targets) {
        this.targets = targets;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}

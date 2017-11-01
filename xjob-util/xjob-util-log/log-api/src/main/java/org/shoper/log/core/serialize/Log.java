package org.shoper.log.core.serialize;

import org.shoper.log.core.config.Constans;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.Transient;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by ShawnShoper on 2017/4/17.
 * 日志基本数据结构
 */
public class Log implements Serializable {
    private String channel;
    //日志
    private String application;
    //时间戳
    private long time;
    //日志内容格式
    private String contentType = Constans.TYPE_STRING;
    //来源地址 HOST:PORT
    private String host;
    private int port;
    private int pid;
    private String className;
    private String methodName;
    private int lineNumber;
    private Business business;

    @Transient
    public String getChannel() {
        return channel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Business getBusiness() {
        return business;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String serializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static Log deserializer(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(data, Log.class);
    }

    public Log() {
    }

    public Log(String channel, String application, long time, String contentType, String host, int port, int pid, String className, String methodName, int lineNumber, Business business) {
        this.channel = channel;
        this.application = application;
        this.time = time;
        this.contentType = contentType;
        this.pid = pid;
        this.host = host;
        this.port = port;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.business = business;
    }

    @Override
    public String toString() {
        return "Log{" +
                "channel='" + channel + '\'' +
                ", application='" + application + '\'' +
                ", time=" + time +
                ", contentType='" + contentType + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", pid=" + pid +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNumber=" + lineNumber +
                ", business=" + business +
                '}';
    }
}

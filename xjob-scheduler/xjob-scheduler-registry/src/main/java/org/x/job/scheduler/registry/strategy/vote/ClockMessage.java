package org.x.job.scheduler.registry.strategy.vote;

/**
 * 时钟机消息体
 */
public class ClockMessage {
    private String message;
    private long timestamp;
    private int appNo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAppNo() {
        return appNo;
    }

    public void setAppNo(int appNo) {
        this.appNo = appNo;
    }
}

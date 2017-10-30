package org.x.job.commons.job.model;

import java.util.Date;

/**
 * 任务参数
 * 见名知义，不作赘述
 * @author Eightmonth
 */
public class JobParam {
    private String tid;
    private String url;
    private String cron;
    private String concurrent;
    private boolean available;
    private Date startTime;
    private Date endTime;
    // orderTime demo: date1,date2,date3... 暂时不用
    private String orderTime;

    public String getTid() { return tid;}

    public void setTid(String tid) { this.tid = tid;}

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

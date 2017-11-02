package org.x.job.scheduler.executors;

import org.shoper.commons.core.MD5Util;

import java.util.Objects;

public class Executor {
    private String id;
    private String host;
    private int port;
    private String serviceId;
    private Status status;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Executor(String id,String host, int port, String serviceId, Status status) {
        this.host = host;
        this.port = port;
        this.serviceId = serviceId;
        this.status = status;
    }

    public Executor(String host, int port, String serviceId) {
        this(null,host,port,serviceId,Status.UP);
    }

    @Override
    public String toString() {
        return "Executor{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", serviceId='" + serviceId + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Executor)) return false;
        Executor executor = (Executor) o;
        return id != null ? id.equals(executor.id) : executor.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

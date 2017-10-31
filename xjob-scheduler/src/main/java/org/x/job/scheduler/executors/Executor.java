package org.x.job.scheduler.executors;

public class Executor {
    private String host;
    private int port;
    private String serviceId;
    private Status status;

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

    public Executor() {
    }

    public Executor(String host, int port, String serviceId, Status status) {
        this.host = host;
        this.port = port;
        this.serviceId = serviceId;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Executor)) return false;

        Executor executor = (Executor) o;

        if (port != executor.port) return false;
        if (!host.equals(executor.host)) return false;
        if (!serviceId.equals(executor.serviceId)) return false;
        return status == executor.status;
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + port;
        result = 31 * result + serviceId.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Executor{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", status=" + status +
                '}';
    }

}

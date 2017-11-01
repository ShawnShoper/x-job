package org.shoper.log.util.config;

/**
 * Created by ShawnShoper on 2016/11/11.
 */
public class ProducerConfiguration {
    private String acks;
    private String batchSize;
    private String bufferMemory;
    private String clientId;
    private String compressionType;
    private String connectionsMaxIdle;
    private String keySerializer;
    private String linger;
    private String maxBlock;
    private String maxInFlightRequestsPerConnection;
    private String maxRequestSize;
    private String metadataMaxAge;
    private String metricReporters;
    private String metricsNumSamples;
    private String metricsSampleWindow;
    private String partitionerClass;
    private String receiveBufferBytes;
    private String reconnectBackoff;
    private String requestTimeout;
    private String retries;
    private String retryBackoff;
    private String sendBufferBytes;
    private String valueSerializer;

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }


    public String getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getConnectionsMaxIdle() {
        return connectionsMaxIdle;
    }

    public void setConnectionsMaxIdle(String connectionsMaxIdle) {
        this.connectionsMaxIdle = connectionsMaxIdle;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getLinger() {
        return linger;
    }

    public void setLinger(String linger) {
        this.linger = linger;
    }

    public String getMaxBlock() {
        return maxBlock;
    }

    public void setMaxBlock(String maxBlock) {
        this.maxBlock = maxBlock;
    }

    public String getMaxInFlightRequestsPerConnection() {
        return maxInFlightRequestsPerConnection;
    }

    public void setMaxInFlightRequestsPerConnection(String maxInFlightRequestsPerConnection) {
        this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getMetadataMaxAge() {
        return metadataMaxAge;
    }

    public void setMetadataMaxAge(String metadataMaxAge) {
        this.metadataMaxAge = metadataMaxAge;
    }

    public String getMetricReporters() {
        return metricReporters;
    }

    public void setMetricReporters(String metricReporters) {
        this.metricReporters = metricReporters;
    }

    public String getMetricsNumSamples() {
        return metricsNumSamples;
    }

    public void setMetricsNumSamples(String metricsNumSamples) {
        this.metricsNumSamples = metricsNumSamples;
    }

    public String getMetricsSampleWindow() {
        return metricsSampleWindow;
    }

    public void setMetricsSampleWindow(String metricsSampleWindow) {
        this.metricsSampleWindow = metricsSampleWindow;
    }

    public String getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(String partitionerClass) {
        this.partitionerClass = partitionerClass;
    }

    public String getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public void setReceiveBufferBytes(String receiveBufferBytes) {
        this.receiveBufferBytes = receiveBufferBytes;
    }

    public String getReconnectBackoff() {
        return reconnectBackoff;
    }

    public void setReconnectBackoff(String reconnectBackoff) {
        this.reconnectBackoff = reconnectBackoff;
    }

    public String getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getRetryBackoff() {
        return retryBackoff;
    }

    public void setRetryBackoff(String retryBackoff) {
        this.retryBackoff = retryBackoff;
    }

    public String getSendBufferBytes() {
        return sendBufferBytes;
    }

    public void setSendBufferBytes(String sendBufferBytes) {
        this.sendBufferBytes = sendBufferBytes;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}

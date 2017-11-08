package org.x.job.scheduler.registry;


import org.shoper.commons.core.MD5Util;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.registry.constant.Duty;

import java.util.Objects;
@ConfigurationProperties(prefix = "org.x.job.schedule")
@Component
public class Scheduler {
    private String host;
    private String port;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getId() {
        if (Objects.isNull(this.id))
            if (Objects.isNull(this.id))
                this.id = MD5Util.getMD5Code(this.getClass().getName()+host + port);
        return id;
    }

}

package org.x.job.scheduler.registry;


import org.shoper.commons.core.MD5Util;

import java.util.Objects;

public class Scheduler {
    private String host;
    private String port;
    private String id;

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

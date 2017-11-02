package org.x.job.scheduler.executors;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKPool;
import org.x.job.util.zookeeper.ZKWatcher;

import java.util.Objects;

@Component
public class ExecutorRegistry {

    @Value("${spring.cloud.zookeeper.discovery.instance-host}")
    private String zkHost;
    @Value("${spring.cloud.zookeeper.discovery.instance-port}")
    public int zkPort;
    ZKClient zkClient;

    public void registry() {
        initZK();
    }

    private void initZK() {
        zkClient = ZKPool.creatZkClient(this.getClass().getName(), zkHost, zkPort, 5000, new ExecutorZKWatch());
    }


    class ExecutorZKWatch extends ZKWatcher {
        @Override
        public void sessionExpired() {
            super.sessionExpired();
        }

        @Override
        public void childrenNodeChangeProcess(WatchedEvent event) {
            super.childrenNodeChangeProcess(event);
        }

        @Override
        public void dataChangeProcess(WatchedEvent event) {
            super.dataChangeProcess(event);
        }

        @Override
        public void nodeDeleteProcess(WatchedEvent event) {
            super.nodeDeleteProcess(event);
        }

        @Override
        public void nodeCreateProcess(WatchedEvent event) {
            super.nodeCreateProcess(event);
        }
    }
}

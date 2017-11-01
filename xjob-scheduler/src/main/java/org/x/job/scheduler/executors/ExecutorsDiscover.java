package org.x.job.scheduler.executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKPool;
import org.x.job.util.zookeeper.ZKWatcher;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Executor discover.<br/>
 * Be responsible to discover new executor and maintain executor list
 */
@Component
public class ExecutorsDiscover {
    @Autowired
    DiscoveryClient discoveryClient;
    ZKClient zkClient;
    @Value("${spring.cloud.zookeeper.discovery.instance-host}")
    private String zkHost;
    @Value("${spring.cloud.zookeeper.discovery.instance-port}")
    public int zkPort;

    @PostConstruct
    public void init() {
        zkClient = ZKPool.creatZkClient(this.getClass().getName(),zkHost,zkPort,5000,new ZKWatcher());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchExecutorInstance();
            }
        }, 5000L);
    }

    private void fetchExecutorInstance() {
        List<String> services = discoveryClient.getServices();
        Map<String, List<Executor>> serviceInstanceMap = new HashMap<>();
        services.stream().forEach(e -> {
            List<Executor> collect = discoveryClient.getInstances(e).stream()
                    .map(a -> new Executor(a.getHost(), a.getPort(), a.getServiceId(), Status.UP)).collect(Collectors.toList());
            serviceInstanceMap.put(e, collect);
        });
        ExecutorContainerHandler.push(serviceInstanceMap);
    }
}

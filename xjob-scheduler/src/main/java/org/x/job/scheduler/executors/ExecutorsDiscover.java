package org.x.job.scheduler.executors;

import org.apache.zookeeper.ZKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;
import org.springframework.stereotype.Component;

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
    @Autowired
    ZookeeperDiscoveryClient zookeeperDiscoveryClient;
    @PostConstruct
    public void init() {
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
            List<Executor> collect = discoveryClient.getInstances(e).stream().map(a -> new Executor(a.getHost(), a.getPort(), a.getServiceId(), Status.UP)).collect(Collectors.toList());
            serviceInstanceMap.put(e, collect);
        });
        ExecutorContainer.push(serviceInstanceMap);
    }
}

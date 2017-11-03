package org.x.job.scheduler.executor.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.executor.Executor;

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
            List<Executor> collect = discoveryClient.getInstances(e).stream()
                    .map(a -> new Executor(a.getServiceId(), a.getHost(), a.getPort(), e, Status.UP)).collect(Collectors.toList());
            serviceInstanceMap.put(e, collect);
        });
        ExecutorContainerHandler.push(serviceInstanceMap);
    }
}

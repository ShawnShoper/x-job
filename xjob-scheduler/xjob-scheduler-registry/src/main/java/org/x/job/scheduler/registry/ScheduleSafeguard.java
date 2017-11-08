package org.x.job.scheduler.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@EnableConfigurationProperties(Scheduler.class)
public class ScheduleSafeguard {
    @Autowired
    ScheduleRegistry scheduleRegistry;
    @Autowired
    Scheduler scheduler;
    @PostConstruct
    public void init() throws InterruptedException {
        scheduleRegistry.connecte();
        if(!scheduleRegistry.registryMasterExecutorInfo(scheduler))
            scheduleRegistry.disconnecte();
    }
}

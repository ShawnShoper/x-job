package org.x.job.scheduler.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.registry.constant.Duty;
import org.x.job.scheduler.registry.strategy.ClusterMasterSelect;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

@Component
@EnableConfigurationProperties(Scheduler.class)
public class ScheduleSafeguard {
    @Autowired
    ClusterMasterSelect clusterMasterSelect;
    @Autowired
    Scheduler scheduler;
    private Duty duty;
    @Autowired
    ExecutorService executorService;

    @PostConstruct
    public void init() {

    }

    /**
     * Preempt for master ...
     *
     * @throws InterruptedException
     */
    public void preemptForMaster() throws InterruptedException {
        if (!clusterMasterSelect.selectMaster())
            duty = Duty.SLAVER;
        else
            duty = Duty.MASTER;

        if (duty.equals(Duty.MASTER)) {
            //TODO Do task to redis.
        }
    }
}

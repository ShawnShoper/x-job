package org.x.job.scheduler.registry.strategy.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.registry.ZKModule;
import org.x.job.scheduler.registry.ZookeeperInfo;

/**
 * 用于作为时间同步机.
 */
@Component
public class ClockSynchronization extends ZKModule{
    @Autowired
    ZookeeperInfo zookeeperInfo;
    @Override
    public int start() {
        setZkInfo(zookeeperInfo);
        return super.start();
    }


}

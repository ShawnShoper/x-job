package org.x.job.scheduler.registry.strategy.vote;

import org.x.job.scheduler.registry.strategy.ClusterMasterSelect;

/**
 * 投票选举策略获取master.
 */
public class VoteAlgorithmSelect implements ClusterMasterSelect {



    @Override
    public boolean selectMaster() {
        return false;
    }
}

package org.x.job.scheduler.registry.strategy;

/**
 * 投票选举策略获取master.
 */
public class VoteAlgorithmSelect implements  ClusterMasterSelect{

    @Override
    public boolean selectMaster() {
        return false;
    }
}

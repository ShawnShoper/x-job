package org.x.job.executor.election;

import org.apache.zookeeper.CreateMode;
import org.x.job.util.zookeeper.ZKClient;

public class DefaultElection implements Election {
    private static final String ELECTION_PATH = "/election";
    private ZKClient zkClient;

    public DefaultElection(ZKClient zkClient){
        this.zkClient = zkClient;
    }
    @Override
    public Boolean doIt() {
        try {
            zkClient.createNode(ELECTION_PATH, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

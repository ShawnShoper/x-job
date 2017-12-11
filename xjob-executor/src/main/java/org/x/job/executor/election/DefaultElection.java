package org.x.job.executor.election;

import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;
import org.x.job.util.zookeeper.ZKClient;

@Service("election")
public class DefaultElection implements Election {
    private static final String ELECTION_PATH = "/election";
    private ZKClient zkClient = new ZKClient();

    public DefaultElection(){

    }
    @Override
    public Boolean doElection() {
        try {
            zkClient.createNode(ELECTION_PATH, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void close() {
        zkClient.close();
    }
}

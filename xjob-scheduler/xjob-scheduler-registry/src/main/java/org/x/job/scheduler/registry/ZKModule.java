package org.x.job.scheduler.registry;

import org.apache.zookeeper.WatchedEvent;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKWatcher;

public abstract class ZKModule extends StartableModule {


    /**
     * reconnect zookeeper
     */
    protected void reConnect() {
        ZKModule.this.start();
    }

    ;
    private ZookeeperInfo zookeeperInfo;
    private volatile ZKClient zkClient;

    /**
     * 启动 zk,第一步需要设置{@code setZkInfo(zkInfo)}
     */
    protected void startZookeeper() {
        ZKClient zkClient = new ZKClient(zookeeperInfo.getHost(), zookeeperInfo.getPort(),
                (int) zookeeperInfo.getTimeUnit().toMillis(zookeeperInfo.getTimeout()), new MyWatcher());
        this.zkClient = zkClient;
    }

    protected ZKClient getZkClient() {
        return zkClient;
    }

    protected void sessionExpired() {
        ZKModule.this.reConnect();
    }

    protected void childrenNodeChangeProcess(WatchedEvent event) {
    }

    protected void dataChangeProcess(WatchedEvent event) {
    }

    protected void nodeDeleteProcess(WatchedEvent event) {
    }

    protected void nodeCreateProcess(WatchedEvent event) {
    }

    class MyWatcher extends ZKWatcher {

        @Override
        public void sessionExpired() {
            ZKModule.this.sessionExpired();
        }

        @Override
        public void childrenNodeChangeProcess(WatchedEvent event) {
            ZKModule.this.childrenNodeChangeProcess(event);
        }

        @Override
        public void dataChangeProcess(WatchedEvent event) {
            ZKModule.this.dataChangeProcess(event);
        }

        @Override
        public void nodeDeleteProcess(WatchedEvent event) {
            ZKModule.this.nodeDeleteProcess(event);
        }

        @Override
        public void nodeCreateProcess(WatchedEvent event) {
            ZKModule.this.nodeCreateProcess(event);
        }
    }

    protected ZookeeperInfo getZookeeperInfo() {
        return zookeeperInfo;
    }

    protected void setZkInfo(ZookeeperInfo zookeeperInfo) {
        this.zookeeperInfo = zookeeperInfo;
    }

    @Override
    public int start() {
        try {
            startZookeeper();
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Override
    public void stop() {
        zkClient = getZkClient();
        if (zkClient != null)
            zkClient.close();
        setStarted(false);
    }

}
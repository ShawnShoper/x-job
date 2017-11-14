package org.x.job.scheduler.registry.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.shoper.log.util.annotation.LogModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.registry.Scheduler;
import org.x.job.scheduler.registry.ZookeeperInfo;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKPool;
import org.x.job.util.zookeeper.ZKWatcher;

import java.util.concurrent.locks.ReentrantLock;

import static org.x.job.scheduler.registry.constant.InstanceConst.SCHEDULE_MASTER_NODE;
import static org.x.job.scheduler.registry.constant.InstanceConst.SCHEDULE_SLAVER_NODE;
/**
 * 抢占式夺取master
 */
public class GrabAlgorithmSelect implements ClusterMasterSelect{
    private static Logger logger = LogFactory.getLogger(org.x.job.scheduler.registry.strategy.GrabAlgorithmSelect.class);
    private ZookeeperInfo zookeeperInfo;
    ZKClient zkClient;
    /**
     * Put on  a reentrance
     */
    ReentrantLock reentrantLock = new ReentrantLock(true);

    public void connect() throws InterruptedException {
        reentrantLock.lockInterruptibly();
        try {
            zkClient = ZKPool.creatZkClient(this.getClass().getName(), zookeeperInfo.getHost(), zookeeperInfo.getPort(), 5000, new ScheudleZKWatcher());
        } finally {
            reentrantLock.unlock();
        }
    }

    public void disconnect() {
        this.zkClient.close();
        this.zkClient = null;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registry schedule master info to registry center
     *
     * @throws InterruptedException
     */
    public boolean registryMasterExecutorInfo(Scheduler scheduled) throws InterruptedException {
        boolean isMaster = false;
        reentrantLock.lockInterruptibly();
        try {
            isMaster = zkClient.createNode(SCHEDULE_MASTER_NODE, objectMapper.writeValueAsString(scheduled), CreateMode.EPHEMERAL);
            if (isMaster) {
                if (logger.isInfoEnable())
                    logger.info("Create node '%s'", SCHEDULE_SLAVER_NODE);
            }
        } catch (KeeperException e) {
            logger.error("zookeeper create %s failed...", e.getPath(), e);
        } catch (JsonProcessingException e) {
            logger.error("Schedule to json failed..", e);
        }
        reentrantLock.unlock();
        return isMaster;
    }

    @Override
    public boolean selectMaster() {
        return false;
    }

    /**
     * Schedule watcher to monitor zookeeper node.
     */
    class ScheudleZKWatcher extends ZKWatcher {
        @Override
        public void sessionExpired() throws Exception {
            reentrantLock.lockInterruptibly();
            try {
                disconnect();
                connect();
            } catch (Exception e) {
                logger.error("Zookeeper connection failed.", e);
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}

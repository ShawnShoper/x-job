package org.x.job.scheduler.registry.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.x.job.scheduler.registry.Scheduler;
import org.x.job.scheduler.registry.ZKModule;
import org.x.job.scheduler.registry.ZookeeperInfo;
import org.x.job.util.zookeeper.ZKClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.x.job.scheduler.registry.constant.InstanceConst.SCHEDULE_MASTER_NODE;
import static org.x.job.scheduler.registry.constant.InstanceConst.SCHEDULE_SLAVER_NODE;
/**
 * 抢占式夺取master
 */
public class GrabAlgorithmSelect extends ZKModule implements ClusterMasterSelect{
    private static Logger logger = LogFactory.getLogger(GrabAlgorithmSelect.class);
    private ZookeeperInfo zookeeperInfo;

    @Override
    public int start() {
        setZkInfo(zookeeperInfo);
        return super.start();
    }

    /**
     * Put on  a reentrance
     */
    ReentrantLock reentrantLock = new ReentrantLock(true);

//    public void connect() throws InterruptedException {
//        reentrantLock.lockInterruptibly();
//        try {
//            zkClient = ZKPool.creatZkClient(this.getClass().getName(), zookeeperInfo.getHost(), zookeeperInfo.getPort(), 5000, new ScheudleZKWatcher());
//        } finally {
//            reentrantLock.unlock();
//        }
//    }

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
            isMaster = this.getZkClient().createNode(SCHEDULE_MASTER_NODE, objectMapper.writeValueAsString(scheduled), CreateMode.EPHEMERAL);
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

//    /**
//     * Schedule watcher to monitor zookeeper node.
//     */
//    class ScheudleZKWatcher extends ZKWatcher {
//        @Override
//        public void sessionExpired() throws Exception {
//            reentrantLock.lockInterruptibly();
//            try {
//                disconnect();
//                connect();
//            } catch (Exception e) {
//                logger.error("Zookeeper connection failed.", e);
//            } finally {
//                reentrantLock.unlock();
//            }
//        }
//    }
}

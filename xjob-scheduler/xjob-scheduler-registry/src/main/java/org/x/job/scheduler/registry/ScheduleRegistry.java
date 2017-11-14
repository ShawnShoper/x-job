package org.x.job.scheduler.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKPool;
import org.x.job.util.zookeeper.ZKWatcher;

import java.util.concurrent.locks.ReentrantLock;

import static org.x.job.scheduler.registry.constant.InstanceConst.SCHEDULE_MASTER_NODE;

@Component
public class ScheduleRegistry extends ZKModule{
    private static Logger logger = LogFactory.getLogger(ScheduleRegistry.class);
    @Autowired
    ZookeeperInfo zookeeperInfo;

    @Override
    public int start() {
        setZkInfo(zookeeperInfo);
        return super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Value("${spring.cloud.zookeeper.discovery.instance-host}")
    private String zkHost;
    @Value("${spring.cloud.zookeeper.discovery.instance-port}")
    public int zkPort;
    ZKClient zkClient;
    /**
     * Reentrance lock
     */
    ReentrantLock reentrantLock = new ReentrantLock(true);

    public void connect() throws InterruptedException {
        reentrantLock.lockInterruptibly();
        try {
            zkClient = ZKPool.creatZkClient(this.getClass().getName(), zkHost, zkPort, 5000, new ScheudleRegistryZKWatcher());
        } finally {
            reentrantLock.unlock();
        }
    }

    public void disconnect() {
        this.zkClient.close();
        this.zkClient = null;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    public void registryExecutorInfo(Scheduler scheduled) throws InterruptedException {
        reentrantLock.lockInterruptibly();
        try {
            zkClient.createNode(SCHEDULE_MASTER_NODE + "/" + scheduled.getId(), objectMapper.writeValueAsString(scheduled), CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            logger.error("zookeeper create %s failed...", e.getPath(), e);
        } catch (JsonProcessingException e) {
            logger.error("Schedule to json failed..", e);
        }
        reentrantLock.unlock();
    }

    /**
     * Schedule watcher to monitor zookeeper node.
     */
    class ScheudleRegistryZKWatcher extends ZKWatcher {
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

package org.x.job.scheduler.executor.discovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.shoper.log.util.annotation.LogModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.x.job.scheduler.executor.Executor;
import org.x.job.util.zookeeper.ZKClient;
import org.x.job.util.zookeeper.ZKPool;
import org.x.job.util.zookeeper.ZKWatcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static org.x.job.scheduler.executor.constant.InstanceConst.EXECUTORINFO_NODE;

/**
 * Registry for a registed  executor
 */
@Component
@LogModel("Executor registry")
public class ExecutorRegistry {
    private static Logger logger = LogFactory.getLogger(ExecutorRegistry.class);
    @Value("${spring.cloud.zookeeper.discovery.instance-host}")
    private String zkHost;
    @Value("${spring.cloud.zookeeper.discovery.instance-port}")
    public int zkPort;
    ZKClient zkClient;

    ReentrantLock reentrantLock = new ReentrantLock(true);

    public void registry() throws InterruptedException {
        initZK();
    }

    ObjectMapper objectMapper = new ObjectMapper();

    public void registryExecutorInfo(Map<String, List<Executor>> executors) throws InterruptedException {
        reentrantLock.lockInterruptibly();
        try {
            String serviceInfo = objectMapper.writeValueAsString(executors);
            boolean node = zkClient.createNode(EXECUTORINFO_NODE, serviceInfo, CreateMode.EPHEMERAL);
            if(node) {
                if (logger.isInfoEnable())
                    logger.info("Create node '%s'",EXECUTORINFO_NODE);

            }
        } catch (JsonProcessingException e) {
            logger.error("Executors to json failed..", e);
        } catch (KeeperException e) {
            logger.error("zookeeper write executor info failed...", e);
        }
        reentrantLock.unlock();
    }

    private void initZK() throws InterruptedException {
        reentrantLock.lockInterruptibly();
        try {
            zkClient = ZKPool.creatZkClient(this.getClass().getName(), zkHost, zkPort, 5000, new ExecutorZKWatch());
        } catch (Exception e) {
            logger.error("Zookeeper connection failed.", e);
        } finally {
            reentrantLock.unlock();
        }
    }


    class ExecutorZKWatch extends ZKWatcher {
        @Override
        public void sessionExpired() throws Exception {
            logger.debug("Zookeeper connection session expired.");
            super.sessionExpired();
            zkClient.close();
            zkClient = null;
            ExecutorRegistry.this.initZK();

        }

        @Override
        public void childrenNodeChangeProcess(WatchedEvent event) throws Exception {

            super.childrenNodeChangeProcess(event);

        }

        @Override
        public void dataChangeProcess(WatchedEvent event) throws Exception {
            super.dataChangeProcess(event);
        }

        @Override
        public void nodeDeleteProcess(WatchedEvent event) throws Exception {
            super.nodeDeleteProcess(event);
        }

        @Override
        public void nodeCreateProcess(WatchedEvent event) throws Exception {
            super.nodeCreateProcess(event);
        }
    }
}

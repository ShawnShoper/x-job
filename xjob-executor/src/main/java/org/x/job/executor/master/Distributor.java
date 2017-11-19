package org.x.job.executor.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.x.job.commons.job.Job;
import org.x.job.commons.transfer.Fenshou;
import org.x.job.executor.pipeline.PipelineExecutor;
import org.x.job.executor.pipeline.support.PipelineSupport;
import org.x.job.executor.receive.TaskHandler;
import org.x.job.executor.send.MessageSender;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务分发器
 * @author Eightmonth
 */
@Service
public class Distributor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Distributor.class);

    @Autowired
    private PipelineExecutor pipelineExecutor;

    @Autowired
    private MessageSender messageSender;

    public void doDistribute() throws Exception {

        if(LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>>>>> Task action <<<<<<<<");
        List<String> machines = TaskHandler.getMachines();
        List<String> jobs = TaskHandler.getJob();

        if(LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>>>>> Task getting <<<<<<<<");

        for (String addr : machines){
            messageSender.innerSend(addr, jobs);
        }

        if(LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>>>>> Task got end <<<<<<<<");

        if(LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>>>>> Task executing <<<<<<<<");

        // 传入哪个地址，然后根据地址执行哪台的Job，这里可以考虑成多线程执行。
        // 同时或许需要同步锁。（参考），已有threadLocal

        // 2.0 BUG 因为存值用的threadlocal，所以在这里执行将会有问题。后将处理
        for (String addr : machines) {
            executor(addr);
        }
        if(LOGGER.isInfoEnabled())
            LOGGER.info(">>>>>>>>> Task end <<<<<<<<");
    }

    public Job getJobByUUID(String uuid) throws Exception {
        // 根据UUID从第三方存储得到job的文件，进行编译并得到Job对象。
        return null;
    }

    public void executor(String addr) throws Exception {
        System.out.println(String.format("%s start", addr));
        pipelineExecutor.doIt();
        System.out.println(String.format("%s end", addr));
    }
}

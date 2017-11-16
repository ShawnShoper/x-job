package org.x.job.executor.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.x.job.executor.pipeline.support.PipelineSupport;
import org.x.job.executor.receive.TaskHandler;

/**
 * pipeline执行器
 * @author Eightmonth
 */
@Service
public class PipelineExecutor {

    @Autowired
    private Pipeline pipeline;

    /**
     * 当有子类继承自pipeline执行器时，重写该方法可在执行任务前做一些操作
     */
    protected void pre(){

    }

    /**
     * 当有子类继承自pipeline执行器，重写该方法可以执行任务后做一些操作
     */
    protected void post(){

    }

    /**
     * 当有子类继承自pipeline执行器，重写该方法可以执行任务发生异常时做一些操作
     */
    protected void exception(Exception e){

    }

    /**
     * 当有子类继承自pipeline执行器，重写该方法可以执行任务结束后最终做一些操作，参考finally
     */
    protected void last(){

    }

    /**
     * 为了满足单一原则，及扩展性，特意把执行方法放到这来
     * @throws Exception
     */
    public void doIt() throws Exception {
        try {
            pre();
            pipeline.doTask(PipelineSupport.jobToSection(TaskHandler.getJob().get()));
            post();
        } catch (Exception e){
            exception(e);
        } finally {
            last();
        }
    }
}

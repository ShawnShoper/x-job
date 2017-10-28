package org.xjob.task.model;

import org.xjob.task.Exception.StartupException;
import org.xjob.task.Job;

import java.util.UUID;

/**
 * 注册任务时，系统缺省参数配置
 * @author Eightmonth
 */
public abstract class BaseJob implements Job {

    protected JobParam param;

    @Override
    public void start() throws StartupException {
        System.out.println("任务开始执行了。。。。");
    }

    @Override
    public void stop() {
        System.out.println("任务被强行结束了。。。。");
    }

    @Override
    public void shutdown() {
        System.out.println("任务执行完就结束了。。。。");
    }

    public void defaultConfig() throws Exception {
        param = null;
    }

    @Override
    public void changeError(JobParam param) throws Exception {
        this.param = param;
    }

    @Override
    public String tid() {
        /**
         * 算法有待优化
         */
        return UUID.randomUUID().toString();
    }

    public JobParam getParam() {
        return param;
    }

}

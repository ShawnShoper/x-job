package org.x.job.commons.job.model;

import org.x.job.commons.increment.Snowflake;
import org.x.job.commons.job.Exception.StartupException;
import org.x.job.commons.job.Job;

import java.util.UUID;

/**
 * 注册任务时，系统缺省参数配置
 * @author Eightmonth
 */
public abstract class BaseJob implements Job {

    private Snowflake snowflake = new Snowflake(12);

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
        return Long.valueOf(snowflake.next()).toString();
    }

    public JobParam getParam() {
        return param;
    }

}

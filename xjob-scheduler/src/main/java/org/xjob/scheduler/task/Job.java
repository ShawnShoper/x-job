package org.xjob.scheduler.task;

import org.xjob.scheduler.task.Exception.StartupException;
import org.xjob.scheduler.task.model.JobParam;

/**
 * 定义一个Job必须实现当前接口
 * 若非Job接口的扩展类，将在系统中不承认非实现Job接口的job.
 * @author Eightmonth
 */
public interface Job {

    void start() throws StartupException;

    /**
     * 强行关闭
     */
    void stop();

    /**
     * 软关闭
     */
    void shutdown();

    /**
     * 定义一个任务时，注册相对应的参数。
     * @param param 任务参数
     * @throws Exception
     */
    void afterProperties(JobParam param) throws Exception;

    /**
     * 任务执行时，当发生异常，更换掉参数
     * @param param 发生异常更换的参数
     * @throws Exception
     */
    void changeError(JobParam param) throws Exception;

    /**
     * 拿到当前任务ID
     */
    String tid();
}

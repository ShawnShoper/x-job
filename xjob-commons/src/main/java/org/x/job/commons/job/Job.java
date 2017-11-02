package org.x.job.commons.job;

import org.x.job.commons.job.Exception.StartupException;
import org.x.job.commons.job.model.JobParam;

/**
 * 定义一个Job必须实现当前接口
 * 若非Job接口的扩展类，将在系统中不承认非实现Job接口的job.
 * @version 1.0
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

    /**
     * Job中的核心方法之一，
     * 用于执行任务，参照Runnable#run()
     * 方法内产生的所有异常需生产方自行处理，当前方法不提供事务保障。
     * @return 返回对象提供下一步操作使用或结束。
     */
    default Object doJob(){
        return null;
    }
}

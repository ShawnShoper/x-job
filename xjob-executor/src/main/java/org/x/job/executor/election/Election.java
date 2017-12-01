package org.x.job.executor.election;

/**
 * 选举
 * 单调方向选举接口，如：抢占式
 * @author Eightmonth
 */
@FunctionalInterface
public interface Election {

    /**
     * 选举方法
     * @return true 成功为master false 成功为slave
     */
    Boolean doIt();
}

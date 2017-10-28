package org.xjob.container;

import java.util.List;

/**
 * 任务容器
 * 概念：任何要执行的任务必须存储在容器里面才可以被取出到执行器执行。
 * 所有种类的任务对于系统来说，都是私有的。
 * 容器对于系统来说是任务公有访问方法，类比：setter/getter
 * @author Eightmonth
 */
public interface TaskContainer<Job>{

    void clear();

    void failBack(Job job);

    List<Job> pullTask(int size);

}

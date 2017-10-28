package org.xjob.container.repo;

import org.xjob.container.TaskContainer;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 具有优先级功能的任务容器。
 * 优先级功能通过继承并发包的集合类实现
 * @see PriorityBlockingQueue
 * @param <Job> 任务种类
 * @author Eightmonth
 */
public class PriorityTaskContainer<Job> extends PriorityBlockingQueue<Job> {

    public PriorityTaskContainer(){}

    public PriorityTaskContainer(int initialCapacity) {
        super(initialCapacity);
    }

    public PriorityTaskContainer(Collection<? extends Job> c) {
        super(c);
    }

    public void failBack(Job job) {
        put(job);
    }

    public PriorityBlockingQueue<Job> pullTask(int size){
        PriorityBlockingQueue<Job> queues = new PriorityTaskContainer<>(size);
        for(int i = 0; i < size; i++){
            queues.put(poll());
        }
        return queues;
    }
}

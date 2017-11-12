package org.x.job.executor.pipeline;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 整个系统的最后一个整体。
 * 所有在一个流程的任务，根据用户定义的任务的顺序及特性，
 * 由系统生成一个section。
 * section最后被注入到pipeline里面中，最后按照定义的顺序执行。
 *
 * @author Eightmonth
 */
@Component
public class Pipeline {

    public void doTask(Section sct){
        sct.doStm();
        if(!Objects.isNull(sct.getNext()))
            doTask(sct.getNext());
    }
}

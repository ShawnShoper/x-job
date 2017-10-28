package org.xjob.task.demo;

import org.xjob.task.model.BaseJob;
import org.xjob.task.model.JobParam;

/**
 * 样例任务种类
 * @author Eightmonth
 */
public class DemoJob extends BaseJob{

    @Override
    public void afterProperties(JobParam param) throws Exception {
        super.param = param;
    }
}

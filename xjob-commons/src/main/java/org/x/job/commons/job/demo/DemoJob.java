package org.x.job.commons.job.demo;

import org.x.job.commons.job.model.BaseJob;
import org.x.job.commons.job.model.JobParam;

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

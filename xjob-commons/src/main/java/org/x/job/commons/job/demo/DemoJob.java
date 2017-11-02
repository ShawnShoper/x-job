package org.x.job.commons.job.demo;

import org.x.job.commons.job.model.BaseJob;
import org.x.job.commons.job.model.JobParam;

/**
 * 样例任务种类
 * @author Eightmonth
 */
public class DemoJob extends BaseJob{

    private int i;

    public DemoJob(int i) {
        this.i = i;
    }

    @Override
    public void afterProperties(JobParam param) throws Exception {
        super.param = param;
    }

    @Override
    public Object doJob() {
        System.out.println("job"+i);
        return null;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}

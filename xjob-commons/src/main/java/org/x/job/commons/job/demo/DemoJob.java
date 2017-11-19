package org.x.job.commons.job.demo;

import org.x.job.commons.job.model.BaseJob;
import org.x.job.commons.job.model.JobParam;

/**
 * 样例任务种类
 * @author Eightmonth
 */
public class DemoJob extends BaseJob{

    private int i;

    private String s;

    public DemoJob(int i) {
        this.i = i;
    }

    public DemoJob(String s) { this.s = s;}

    @Override
    public void afterProperties(JobParam param) throws Exception {
        super.param = param;
    }

    @Override
    public Object doJob() {
        System.out.println("job"+s);
        return null;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}

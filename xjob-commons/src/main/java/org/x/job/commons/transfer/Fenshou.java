package org.x.job.commons.transfer;

import org.x.job.commons.job.Job;

import java.util.List;
import java.util.Map;

/**
 * 分手类
 * 调度器与执行器的最后一个交互步骤
 * @author Eightmonth
 */
public class Fenshou {
    private List<String> machines;
    private List<Job> jobs;
    private Map<String, Object> others;

    public List<String> getMachines() {
        return machines;
    }

    public void setMachines(List<String> machines) {
        this.machines = machines;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }
}

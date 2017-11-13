package org.x.job.commons.transfer;

import java.util.List;
import java.util.Map;

/**
 * 分手类
 * 调度器与执行器的最后一个交互步骤
 * @author Eightmonth
 */
public class Fenshou {
    private List<String> machines;

    private List<String> job;

    private Map<String, Object> others;

    public Fenshou(){}

    public Fenshou(List<String> job) {
        this.job = job;
    }

    public Fenshou(List<String> job, Map<String, Object> others) {
        this.job = job;
        this.others = others;
    }

    public void setJob(List<String> job) {
        this.job = job;
    }

    public List<String> getJob() {
        return job;
    }

    public List<String> getMachines() {
        return machines;
    }

    public void setMachines(List<String> machines) {
        this.machines = machines;
    }


    public Map<String, Object> getOthers() {
        return others;
    }

    public void setOthers(Map<String, Object> others) {
        this.others = others;
    }
}

package org.x.job.executor.pipeline;

import org.x.job.commons.job.Job;

/**
 * Pipeline 里面的重要组成部分,起流程的作用，方便任务按部就班的进行.
 * 同时也是任务在通道内的载体
 * @author Eightmonth
 */
public class Section {

    /**
     * 流程步骤，可为空，可有下一步。
     * Section sct = new Section();
     * sct.doStm();
     * if(!Objets.isNull(sct.getNext()))
     *      stc.getNext().doStm();
     */
    private Section next;

    /**
     * 任务托管
     */
    private Job job;

    public Section(Section next, Job job) {
        this.next = next;
        this.job = job;
    }

    public void doStm(){
        job.doJob();
    }

    public Section getNext() {
        return next;
    }

    public void setNext(Section next) {
        this.next = next;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}

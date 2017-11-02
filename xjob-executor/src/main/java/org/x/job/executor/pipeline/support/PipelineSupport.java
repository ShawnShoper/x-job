package org.x.job.executor.pipeline.support;

import org.x.job.commons.job.Job;
import org.x.job.commons.job.demo.DemoJob;
import org.x.job.executor.pipeline.Pipeline;
import org.x.job.executor.pipeline.Section;

import java.util.*;

/**
 * 把一个整体的Job生成Section
 *
 * @author Eightmonth
 */
public class PipelineSupport {

    public static Section fixSection(List<Job> jobList){
        Collections.reverse(jobList);
        return push(null, jobList, 0);
    }

    public static Section push(Section sct, List<Job> jobs, int i){
        if(i == jobs.size())
            return sct;
        Section s = new Section(sct, jobs.get(i));
        return push(s, jobs, ++i);
    }

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();
        Job job1 = new DemoJob(1);
        Job job2 = new DemoJob(2);
        Job job3 = new DemoJob(3);
        Job job4 = new DemoJob(4);
        Job job5 = new DemoJob(5);

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        jobs.add(job5);

        Pipeline pipeline = new Pipeline();
        pipeline.doTask(fixSection(jobs));
    }
}

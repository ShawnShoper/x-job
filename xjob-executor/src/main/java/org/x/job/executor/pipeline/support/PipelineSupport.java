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

    public static Section jobToSection(List<String> jobUUIDs) throws Exception {
        return fixSection(fixJob(jobUUIDs));
    }

    public static List<Job> fixJob(List<String> jobUUIDs) throws Exception {
        // 这里待处理，把JobUUIDs转成job集合，先做个伪代码
        return new ArrayList<>();
    }

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
}

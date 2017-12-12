package org.x.job.executor.pipeline.support;

import org.x.job.commons.job.Job;
import org.x.job.commons.job.demo.DemoJob;
import org.x.job.commons.utils.HDFSUtils;
import org.x.job.dynamiccompile.JDKCompile;
import org.x.job.executor.pipeline.Pipeline;
import org.x.job.executor.pipeline.Section;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        List<Job> jobs = new ArrayList<>();
        for(String uuids : jobUUIDs){
            jobs.add(getJobByPath(uuids));
        }
        return jobs;
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

    private static Job getJobByPath(String path){
        try {
            byte[] javaFile = HDFSUtils.readHDFSFile(path);
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(javaFile);

            Class<?> class1 = JDKCompile.getClass(file);
            Object object = class1.newInstance();
            return (Job) object;
        } catch (Exception e){
            return null;
        }
    }
}

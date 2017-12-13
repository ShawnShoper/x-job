package org.x.job.executor.pipeline.support;

import org.x.job.commons.increment.Snowflake;
import org.x.job.commons.job.Job;
import org.x.job.commons.utils.HDFSUtils;
import org.x.job.dynamiccompile.JDKCompile;
import org.x.job.executor.pipeline.Section;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 把一个整体的Job生成Section
 *
 * @author Eightmonth
 */
public class PipelineSupport {

    private static String defaultPath = System.getProperty("user.dir")+"/tmp";

    public static String nextRandomName(){
        return defaultPath + "/" +new Snowflake(randomSuffix());
    }

    private static int randomSuffix(){
        Random random = new Random(1024);
        int max = 0;
        for(int i = 0; i < 10; i++){
            int curr = random.nextInt();
            if(i == 0) max = random.nextInt();
            else if(max < curr) max = curr;
        }
        return max;
    }

    private PipelineSupport(){}

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
        File file = new File(nextRandomName());
        try (FileOutputStream fos = new FileOutputStream(file)){
            byte[] javaFile = HDFSUtils.readHDFSFile(path);
            fos.write(javaFile);

            Class<?> class1 = JDKCompile.getClass(file);
            Object object = class1.newInstance();
            return (Job) object;
        } catch (Exception e){
            return null;
        }
    }

}

package org.x.job.executor.master;

import org.x.job.commons.job.Job;
import org.x.job.executor.receive.TaskHandler;

import java.util.List;

/**
 * 任务分发器
 * @author Eightmonth
 */
public class Distributor {
    public void doDistribute() throws Exception {
        List<String> machines = TaskHandler.getMachines().get();
        List<String> UUDs = TaskHandler.getJobs().get();
        List<Job> jobs = getJobByUUID(UUDs);

    }

    public List<Job> getJobByUUID(List<String> uuid) throws Exception {
        // 根据UUID从第三方存储得到job的文件，进行编译并得到Job对象。
        return null;
    }
}

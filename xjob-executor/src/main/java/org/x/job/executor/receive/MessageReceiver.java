package org.x.job.executor.receive;

import javafx.concurrent.Task;
import org.x.job.commons.job.Job;
import org.x.job.executor.receive.enums.MessageEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从调度器接收信息并解析
 */
public class MessageReceiver {
    /**
     * 以messageEnum里面的值作为区别map里面的信息
     * @see MessageEnum
     * @param kav
     * @return
     * @throws Exception
     */
    public Boolean doReceive(Map<String, Object> kav) throws Exception {

        List<Job> jobs = new ArrayList<>();
        List<String> machines = new ArrayList<>();

        for(Map.Entry<String, Object> map : kav.entrySet()){
            if(map.getKey().equals(MessageEnum.JOB)){
                jobs.add((Job)map.getValue());
            }
            if(map.getKey().equals(MessageEnum.MACHINE)){
                machines.add(map.getValue().toString());
            }
        }

        TaskHandler.getJobs().set(jobs);
        TaskHandler.getMachines().set(machines);
        return true;
    }
}

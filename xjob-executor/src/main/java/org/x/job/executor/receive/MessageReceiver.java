package org.x.job.executor.receive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.x.job.commons.job.Job;
import org.x.job.commons.transfer.Fenshou;
import org.x.job.executor.master.Distributor;

import java.util.List;
import java.util.Objects;

/**
 * 从调度器接收信息并解析
 * @author  Eightmonth
 */
//@FeignClient("appilcation-name")// 这里待配置
@Service
public class MessageReceiver {
    @Autowired
    private Distributor distributor;

    /**
     * 从执行器或调度器接收任务参数
     * @param fenShou 接收参数封装对象
     * @return 返回是否接收成功，true:成功， false：失败
     * @throws Exception 如果接收失败，反馈接收失败异常
     */
    public Boolean doReceive(Fenshou fenShou) throws Exception {
        Boolean flag = false;
        try{
            // 不允许没有任务
            if(Objects.isNull(fenShou.getJob()))
                return false;

            TaskHandler.setJob(fenShou.getJob());
            if(!Objects.isNull(fenShou.getMachines()))
                TaskHandler.setMachines(fenShou.getMachines());
            if(!Objects.isNull(fenShou.getOthers()))
                TaskHandler.setOthers(fenShou.getOthers());

            // 收到任务后执行。
            distributor.doDistribute();
            flag = true;
        }finally {
            return flag;
        }
    }

    public void innerReceive(List<String> jobs) throws Exception {
        if(Objects.isNull(jobs) || jobs.size() == 0)
            return;
        TaskHandler.setJob(jobs);
    }
}

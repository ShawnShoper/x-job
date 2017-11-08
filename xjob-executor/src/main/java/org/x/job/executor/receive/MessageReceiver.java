package org.x.job.executor.receive;

import org.springframework.stereotype.Component;
import org.x.job.commons.transfer.Fenshou;

/**
 * 从调度器接收信息并解析
 * @author  Eightmonth
 */
@Component
public class MessageReceiver {

    /**
     * 从执行器或调度器接收任务参数
     * @param fenShou 接收参数封装对象
     * @return 返回是否接收成功，true:成功， false：失败
     * @throws Exception 如果接收失败，反馈接收失败异常
     */
    public Boolean doReceive(Fenshou fenShou) throws Exception {
        Boolean flag = false;
        try{
            TaskHandler.getJobs().set(fenShou.getJobs());
            TaskHandler.getMachines().set(fenShou.getMachines());
            TaskHandler.getOthers().set(fenShou.getOthers());
            flag = true;
        }catch (Exception e){
            throw new Exception(e);
        }finally {
            return flag;
        }
    }
}

package org.x.job.executor.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.x.job.commons.transfer.Fenshou;
import org.x.job.executor.feign.ReceiveFeign;
import org.x.job.executor.feign.SendFeign;
import org.x.job.executor.receive.MessageReceiver;

import java.util.List;

/**
 * 分配任务
 * @author Eightmonth
 */
@Service
public class MessageSender {

    @Autowired
    private SendFeign sendFeign;
    @Autowired
    private ReceiveFeign receiveFeign;

    /**
     * 往哪台机器分配任务 （Master -> Slave）
     * @param addr Slave机器地址
     * @param fenshou 任务参数
     * @return 返回true或false，代表成功或失败
     * @throws Exception 发送失败时反馈的异常
     */
    // TODO
    public Boolean doSend(String addr, Fenshou fenshou) throws Exception {
        Boolean flag = false;
        try{
            receiveFeign.doReceive(fenshou);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return flag;
        }
    }

    // TODO 待整理
    public Boolean innerSend(String addr, List<String> jobs) throws Exception {
        Boolean flag = false;
        try{
            sendFeign.innerSend(addr,jobs);
            System.out.println(String.format("%s receive jobID : %s", addr,jobs));
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return flag;
        }
    }
}

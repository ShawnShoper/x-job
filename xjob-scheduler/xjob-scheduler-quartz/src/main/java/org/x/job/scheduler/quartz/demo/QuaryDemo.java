package org.x.job.scheduler.quartz.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * quartz 设置全局项目定时任务
 * spring quartz
 */
@ComponentScan
public class QuaryDemo {

    // 一分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void work() throws  Exception {
        System.out.println("执行调度任务: " + new Date());
    }

    @Scheduled(fixedRate = 5000) //  5000 ms执行一次
    public void play() throws Exception {
        System.out.println("执行Quartz定时器任务： " + new Date());
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void doSomething() throws Exception {
        System.out.println("每2秒执行一个定时任务： " + new Date());
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void goWork() throws Exception {
        System.out.println("每一个小时执行一次的定时任务：" + new Date());
    }
}

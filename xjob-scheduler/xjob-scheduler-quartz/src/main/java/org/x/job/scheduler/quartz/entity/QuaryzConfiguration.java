package org.x.job.scheduler.quartz.entity;

import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Calendar;

@Configuration
public class QuaryzConfiguration {

    /**
     * 把任务实体转换成quartz可执行的任务实体
     * @param task 任务实体
     * @return 返回quratz可以执行的任务实体
     */
    @Bean(name = "jobDetail")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduleTask task) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发
        jobDetail.setConcurrent(false);
        Calendar c = Calendar.getInstance();
        String groupName = "g1";
        String dateString = String.valueOf(c.get(Calendar.YEAR))
                            +
                            String.valueOf(c.get(Calendar.MONTH)+1)
                            +
                            String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                            +
                            String.valueOf(c.get(Calendar.HOUR))
                            +
                            String.valueOf(c.get(Calendar.MINUTE))
                            +
                            String.valueOf(c.get(Calendar.SECOND));

        // 任务名称
        jobDetail.setName(dateString);
        // 任务分组
        jobDetail.setGroup(groupName);
        // 任务实体
        jobDetail.setTargetObject(task);
        // 任务方法
        jobDetail.setTargetMethod("sayHello");
        return jobDetail;
    }

    /**
     * 根据任务生成trigger
     * @param jobDetail Quartz可执行的任务实体
     * @return 返回触发器
     */
    public CronTriggerFactoryBean cronJobTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(jobDetail.getObject());
        trigger.setCronExpression("0 30 20 * * ?");
        trigger.setName("HelloTrigger");
        return trigger;
    }

    public SchedulerFactoryBean schedulerFactory(Trigger cronJobTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 用于quartz集群，QuartzScheduler 启动时更新已存在的Job
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        bean.setTriggers(cronJobTrigger);
        return bean;
    }
}

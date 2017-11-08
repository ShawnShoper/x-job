package org.x.job.scheduler.core.conf;

import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CommonConfig {
    private static Logger logger = LogFactory.getLogger(CommonConfig.class);
    @Value("")
    @Bean
    public ExecutorService executorService(){
        return Executors.newCachedThreadPool(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            if(logger.isDebugEnable())
                logger.debug("Supply a new Thread.Thread id-%s Thread name-%s, Thread priority-%s, Thread is daemon-%s, Thread group-%s",thread.getId(),thread.getName(),thread.getPriority(),thread.isDaemon(),thread.getThreadGroup());
            return thread;
        });
    }
}

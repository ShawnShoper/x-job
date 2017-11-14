package org.x.job.scheduler.registry.strategy.vote;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 观察者服务<br>
 *     用于处理投票产生的脑裂问题.
 */
@Component
public class ObserverComponet {

    @PreDestroy
    public void destroy(){
        //TODO 销毁Observer服务器的信息

    }

    @PostConstruct
    public void construct(){
        //TODO 初始化Observer服务器的信息
    }
}

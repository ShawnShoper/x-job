package com.daqsoft.log.util.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 指定日志输出通道(指定消息队列topic)
 */
public @interface Channel {
    //指定日志输出通道
    String value();
}

package org.shoper.log.util.annotation;

import java.lang.annotation.*;

/**
 * Created by ShawnShoper on 2017/4/18.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 日志业务模块
 */
public @interface LogModel {
    String value() default "";
}

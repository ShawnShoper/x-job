package com.daqsoft.log.util.annotation;

import java.lang.annotation.*;

/**
 * Content Type.
 *
 * @author shawnshoper
 * @see com.daqsoft.log.util.config.ContentType
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentType {
    com.daqsoft.log.util.config.ContentType value() default com.daqsoft.log.util.config.ContentType.STR;
}

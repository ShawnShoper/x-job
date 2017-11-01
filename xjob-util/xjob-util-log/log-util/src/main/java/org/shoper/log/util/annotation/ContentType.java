package org.shoper.log.util.annotation;

import java.lang.annotation.*;

/**
 * Content Type.
 *
 * @author shawnshoper
 * @see org.shoper.log.util.config.ContentType
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentType {
    org.shoper.log.util.config.ContentType value() default org.shoper.log.util.config.ContentType.STR;
}

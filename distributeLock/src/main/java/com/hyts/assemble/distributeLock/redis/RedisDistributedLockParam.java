package com.hyts.assemble.distributeLock.redis;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisDistributedLockParam {
    String name() default "";
}

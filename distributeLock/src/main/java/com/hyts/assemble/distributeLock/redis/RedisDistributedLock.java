package com.hyts.assemble.distributeLock.redis;

import com.hyts.assemble.distributeLock.base.LockCategory;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisDistributedLock {

    String prefix() default "";

    /**
     * 锁过期时间
     */
    int expireTime() default 30;

    /**
     * 获取锁等待时间
     */
    int waitTime() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String delimiter() default ":";

    LockCategory category() default LockCategory.COMMON;
}

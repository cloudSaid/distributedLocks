package com.hyts.assemble.distributeLock.redis;

import com.hyts.assemble.distributeLock.base.LockCategory;
import com.hyts.assemble.distributeLock.error.RedisDistributedLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;



@Aspect
@Order(4)
@Slf4j
public class RedisDistributedLockAspect {


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisDistributedLockKeyGenerator redisDistributedLockKeyGenerator;


    @Around("execution(public * *(..)) && @annotation(com.hyts.assemble.distributeLock.redis.RedisDistributedLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RedisDistributedLock redisDistributedLock = method.getAnnotation(RedisDistributedLock.class);

        if (StringUtils.isEmpty(redisDistributedLock.prefix())) {
            throw new RuntimeException("lock key can't be null...");
        }

        final String lockKey = redisDistributedLockKeyGenerator.getLockKey(pjp);
        RLock lock = chooseLock(redisDistributedLock,lockKey);
        //key不存在才能设置成功
        Boolean success = null;
        Object proceed = null;

        try {
            success = lock.tryLock(redisDistributedLock.waitTime(), redisDistributedLock.expireTime(), redisDistributedLock.timeUnit());
            if (success) {
                log.debug("tryLock success key [{}]", lockKey);
                proceed = pjp.proceed();
            } else {
                log.error("key is : {" + lockKey + "} tryLock fail ");
                throw new RedisDistributedLockException(lockKey);
            }
        } catch (InterruptedException e) {
            log.error("key is : {" + lockKey + "} tryLock error ", e);
            throw new RedisDistributedLockException(lockKey, e.getMessage());
        } finally {
            if (success) {
                log.debug("unlock [{}]", "key:" + lockKey);
                lock.unlock();
            }
        }
        return proceed;
    }


    private RLock chooseLock(RedisDistributedLock redisDistributedLock, String lockName) {
        LockCategory category = redisDistributedLock.category();
        switch (category) {
            case FAIR:
                return redissonClient.getFairLock(lockName);
        }
        return redissonClient.getLock(lockName);
    }
}

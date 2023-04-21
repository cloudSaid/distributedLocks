
package com.hyts.assemble.distributeLock.redis;

import com.hyts.assemble.distributeLock.common.AbstractDistributeLockSupport;
import com.hyts.assemble.distributeLock.base.DistributeLockParam;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class RedisDistributeLockSupport extends AbstractDistributeLockSupport<RLock> {



    @Autowired
    RedissonClient redissonClient;


    /**
     * 非阻塞方式锁
     * @param distributeLockParam
     * @return
     */
    @Override
    public RLock lock(DistributeLockParam distributeLockParam) {
        distributeLockParam = fullDistributeDefaultValue(distributeLockParam);
        String lockKey = buildLockKey(distributeLockParam);
        RLock rLock = null;
        try {
            switch (distributeLockParam.getLockType()) {
                // 可重入锁
                case REENTRANT_LOCK: {
                    rLock = redissonClient.getLock(lockKey);
                    break;
                }
                // 非公平锁
                case FAIR_LOCK: {
                    rLock = redissonClient.getFairLock(lockKey);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("暂时不支持此种方式的锁!");
                }
            }
            Boolean result = rLock.tryLock(distributeLockParam.getWaitTime(), distributeLockParam.getExpireTime(), distributeLockParam.getTimeUnit());
            return rLock;
        } catch (InterruptedException e) {
            log.error("加锁为阻塞模式下的锁进行失败！", e);
            return rLock;
        }
    }



    @Override
    public void unlock(RLock param, DistributeLockParam distributeLockParam) {
        try {
            param.unlock();
        } catch (Exception e) {
            log.error("解锁为阻塞模式下的锁进行失败！", e);
        }
    }


}

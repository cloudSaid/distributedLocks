
package com.hyts.assemble.distributeLock.common;

import cn.hutool.core.lang.UUID;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.hyts.assemble.distributeLock.base.DistributeLockParam;
import com.hyts.assemble.distributeLock.base.DistributeLockSupport;
import com.hyts.assemble.distributeLock.base.DistributeLockType;
import com.hyts.assemble.distributeLock.redis.RedisDistributedLockParam;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class AbstractDistributeLockSupport<T> implements DistributeLockSupport<T> {



    /**
     * 检验参数
     * @param distributeLockParam
     * @return
     */
    protected DistributeLockParam fullDistributeDefaultValue(DistributeLockParam distributeLockParam){
        Preconditions.checkNotNull(distributeLockParam,"检测到了参数不允许为空！");
        DistributeLockType distributeLockType = distributeLockParam.getLockType();
        distributeLockParam.setLockType(Optional.ofNullable(distributeLockType).orElse(DistributeLockType.FAIR_LOCK));
        distributeLockParam.setExpireTime(Optional.ofNullable(distributeLockParam.getExpireTime()).orElse(DEFAULT_EXPIRE_TIME));
        distributeLockParam.setWaitTime(Optional.ofNullable(distributeLockParam.getExpireTime()).orElse(DEFAULT_WAIT_TIME));
        distributeLockParam.setTimeUnit(Optional.ofNullable(distributeLockParam.getTimeUnit()).orElse(TimeUnit.SECONDS));
        return distributeLockParam;
    }


    /**
     * 构建相关的锁key值
     * @param distributeLockParam
     * @return
     */
    protected String buildLockKey(DistributeLockParam distributeLockParam){
        String lockId = StringUtils.defaultIfEmpty(distributeLockParam.getLockUUid(),
                        UUID.fastUUID().toString());
        distributeLockParam.setLockUUid(lockId);
        String delmiter = StringUtils.defaultIfEmpty(distributeLockParam.getDelimiter(),
                            DEFAULT_DELIMTER);
        distributeLockParam.setDelimiter(delmiter);
        String prefix = StringUtils.defaultIfEmpty(distributeLockParam
                .getLockNamePrefix(),DEFAULT_KEY_PREFIX);
        distributeLockParam.setLockNamePrefix(prefix);
        String lockFullName = "";
        if(!delmiter.equals(DEFAULT_DELIMTER)){
            //todo 待优化
            Joiner joiner = Joiner.on(delmiter).skipNulls();
            lockFullName = joiner.join(prefix,lockId);
        }else{
            lockFullName = DEFAULT_JOINER.join(prefix,lockId);
        }
        return lockFullName;
    }

}

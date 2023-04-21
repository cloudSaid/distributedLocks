
package com.hyts.assemble.distributeLock.base;

import com.hyts.assemble.distributeLock.redis.RedisDistributedLockParam;
import lombok.Data;

import java.util.concurrent.TimeUnit;


@Data
public class DistributeLockParam {

    //锁的id
    private String lockUUid;

    //锁的名称前缀
    private String lockNamePrefix;


    //锁的过期时间
    private Long expireTime;

    //等待时间
    private Long waitTime;

    //时间
    private TimeUnit timeUnit;

    //分隔符
    private String delimiter;

    //锁的类型
    private DistributeLockType lockType;


}

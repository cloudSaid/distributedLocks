
package com.hyts.assemble.distributeLock.base;


public enum DistributeLockType {


    /**
     * 重入锁
     */
    REENTRANT_LOCK,

    /**
     * 非公平锁
     */
    FAIR_LOCK,

    /**
     * 联和锁
     */
    MULTI_LOCK,

    /**
     * 红锁
     */
    RED_LOCK,

    /**
     * 读写锁
     */
    READ_WRITE_LOCK,

    ;


}

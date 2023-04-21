package com.hyts.assemble.distributeLock.error;


public class RedisDistributedLockException extends RuntimeException {

    private String key;

    public RedisDistributedLockException (String key) {
        super("key [" + key + "] tryLock fail");
        this.key = key;
    }

    public RedisDistributedLockException (String key, String errorMessage) {
        super("key [" + key + "] tryLock fail error message :" + errorMessage);
        this.key = key;
    }
}

package com.hyts.assemble.distributeLock.base;

import org.aspectj.lang.ProceedingJoinPoint;


public interface LockKeyGenerator {
    String getLockKey(ProceedingJoinPoint pjp);
}

package io.github.viscent.mtia.ch3.rwlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDowngrade {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void operationWithLockDowngrade() {
        boolean readLockAcquired = false;
        writeLock.lock(); // 申请写锁
        try {
            // 对共享数据进行更新
            // ...
            // 当前线程在持有写锁的情况下申请读锁readLock
            readLock.lock();
            readLockAcquired = true;
        } finally {
            writeLock.unlock();// 释放写锁
        }

        if (readLockAcquired) {
            try {
                // 读取共享数据并据此执行其他操作
                // ...

            } finally {
                readLock.unlock();// 释放读锁
            }
        } else {
            // ...
        }
    }
}

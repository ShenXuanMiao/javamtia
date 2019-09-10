package io.github.viscent.mtia.ch3.rwlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockUsage {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // 读线程执行该方法
    public void reader() {
        readLock.lock(); // 申请读锁
        try {
            // 在此区域读取共享变量
        } finally {
            readLock.unlock();// 总是在finally块中释放锁，以免锁泄漏
        }
    }

    // 写线程执行该方法
    public void writer() {
        writeLock.lock(); // 申请读锁
        try {
            // 在此区域访问（读、写）共享变量
        } finally {
            writeLock.unlock();// 总是在finally块中释放锁，以免锁泄漏
        }
    }
}
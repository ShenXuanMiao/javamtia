package io.github.viscent.mtia.ch3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExplicitLockInfo {
    private static final Lock lock = new ReentrantLock();
    private static int sharedData = 0;

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    try {
                        Thread.sleep(2200000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sharedData = 1;
                } finally {
                    lock.unlock();
                }

            }
        });
        t.start();
        Thread.sleep(100);
        lock.lock();
        try {
            System.out.println("sharedData:" + sharedData);
        } finally {
            lock.unlock();
        }
    }
}
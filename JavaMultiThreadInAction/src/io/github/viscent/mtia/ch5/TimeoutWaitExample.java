package io.github.viscent.mtia.ch5;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.Random;

public class TimeoutWaitExample {
    protected static final Random random = new Random();
    private static final Object lock = new Object();
    private static boolean ready = false;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            // while 一直循环
            while (true) {
                synchronized (lock) {
                    ready = random.nextInt(100) < 5;
                    if (ready) {
                        lock.notify();
                    }
                }
                // 使当前线程暂停一段（随机）时间
                Tools.randomPause(500);
            }
        });
        t.setDaemon(true);
        t.start();
        waiter(1000);
    }

    public static void waiter(final long timeOut) throws InterruptedException {
        if (timeOut < 0) {
            throw new IllegalArgumentException();
        }

        long start = System.currentTimeMillis();
        long waitTime;
        long now;
        synchronized (lock) {
            while (!ready) {
                now = System.currentTimeMillis();
                // 计算剩余等待时间
                waitTime = timeOut - (now - start);
                Debug.info("Remaining time to wait:%sms", waitTime);
                if (waitTime <= 0) {
                    // 等待超时退出
                    break;
                }
                lock.wait(waitTime);
            }// while循环结束

            if (ready) {
                // 执行目标动作
                guardedAction();
            } else {
                // 等待超时，保护条件未成立
                Debug.error("Wait timed out,unable to execution target action!");
            }
        }// 同步块结束
    }

    private static void guardedAction() {
        Debug.info("Take some action.");
        // ...
    }
}

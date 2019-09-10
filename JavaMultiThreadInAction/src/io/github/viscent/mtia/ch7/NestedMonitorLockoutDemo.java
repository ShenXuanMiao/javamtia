package io.github.viscent.mtia.ch7;

import io.github.viscent.mtia.util.Tools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 本程序演示嵌套监视器锁死（线程活性故障）现象。
 *
 * @author Viscent Huang
 */
public class NestedMonitorLockoutDemo {
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
    private int processed = 0;
    private int accepted = 0;

    public static void main(String[] args) throws InterruptedException {
        NestedMonitorLockoutDemo demo = new NestedMonitorLockoutDemo();
        demo.start();
        int i = 0;
        while (i-- < 100000) {
            demo.accept("message" + i);
            Tools.randomPause(100);
        }

    }

    public synchronized void accept(String message) throws InterruptedException {
        // 不要在临界区内调用BlockingQueue的阻塞方法！那样会导致嵌套监视器锁死
        queue.put(message);
        accepted++;
    }

    protected synchronized void doProcess() throws InterruptedException {
        // 不要在临界区内调用BlockingQueue的阻塞方法！那样会导致嵌套监视器锁死
        String msg = queue.take();
        System.out.println("Process:" + msg);
        processed++;
    }

    public void start() {
        new WorkerThread().start();
    }

    public synchronized int[] getStat() {
        return new int[]{accepted, processed};
    }

    class WorkerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    doProcess();
                }
            } catch (InterruptedException e) {
                ;
            }
        }
    }
}
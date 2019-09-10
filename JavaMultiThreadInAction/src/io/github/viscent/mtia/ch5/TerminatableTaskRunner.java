package io.github.viscent.mtia.ch5;

import io.github.viscent.mtia.util.Debug;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminatableTaskRunner implements TaskRunnerSpec {
    // 待处理任务计数器
    public final AtomicInteger reservations = new AtomicInteger(0);
    protected final BlockingQueue<Runnable> channel;
    // 线程停止标记
    protected volatile boolean inUse = true;
    private volatile Thread workerThread;

    public TerminatableTaskRunner(BlockingQueue<Runnable> channel) {
        this.channel = channel;
        this.workerThread = new WorkerThread();
    }

    public TerminatableTaskRunner() {
        this(new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void init() {
        final Thread t = workerThread;
        if (null != t) {
            t.start();
        }
    }

    @Override
    public void submit(Runnable task) throws InterruptedException {
        channel.put(task);
        reservations.incrementAndGet();
    }

    public void shutdown() {
        Debug.info("Shutting down service...");
        inUse = false;// 语句①
        final Thread t = workerThread;
        if (null != t) {
            t.interrupt();// 语句②
        }
    }

    public void cancelTask() {
        Debug.info("Canceling in progress task...");
        workerThread.interrupt();
    }

    class WorkerThread extends Thread {
        @Override
        public void run() {
            Runnable task = null;
            try {
                for (; ; ) {
                    // 线程不再被需要，且无待处理任务
                    if (!inUse && reservations.get() <= 0) {// 语句③
                        break;
                    }
                    task = channel.take();
                    try {
                        task.run();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    // 使待处理任务数减少1
                    reservations.decrementAndGet();// 语句④
                }// for循环结束
            } catch (InterruptedException e) {
                workerThread = null;
            }
            Debug.info("worker thread terminated.");
        }// run方法结束
    }// WorkerThread结束
}
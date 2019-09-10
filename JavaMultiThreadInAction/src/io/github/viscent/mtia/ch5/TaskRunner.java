package io.github.viscent.mtia.ch5;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskRunner {
    protected final BlockingQueue<Runnable> channel;
    protected volatile Thread workerThread;

    public TaskRunner(BlockingQueue<Runnable> channel) {
        this.channel = channel;
        this.workerThread = new WorkerThread();
    }

    public TaskRunner() {
        this(new LinkedBlockingQueue<Runnable>());
    }

    public void init() {
        final Thread t = workerThread;
        if (null != t) {
            t.start();
        }
    }

    public void submit(Runnable task) throws InterruptedException {
        channel.put(task);
    }

    class WorkerThread extends Thread {
        @Override
        public void run() {
            Runnable task = null;
            // 注意：下面这种代码写法实际上可能导致工作者线程永远无法终止！
            // “5.6 对不起，打扰一下：线程中断机制”中我们将会解决这个问题。
            try {
                for (; ; ) {
                    task = channel.take();
                    try {
                        task.run();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }// for循环结束
            } catch (InterruptedException e) {
                // 什么也不做
            }
        }// run方法结束
    }// WorkerThread结束
}
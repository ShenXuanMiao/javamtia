package io.github.viscent.mtia.ch12;

import io.github.viscent.mtia.util.Tools;

/**
 * 伪共享Demo
 *
 * @author Viscent Huang
 */
public class FalseSharingDemo extends Thread {
    final CountingTask task;

    public FalseSharingDemo(CountingTask task) {
        this.task = task;
    }

    public static void main(String[] args) throws Exception {
        int argc = args.length;
        int N;// 工作者线程数
        N = argc > 0 ? Integer.valueOf(args[0]) : Runtime.getRuntime()
                .availableProcessors();
        long iterations;
        iterations = argc > 1 ? Long.valueOf(args[1])
                : 400 * 1000 * 1000L;

        String taskImplClassName;
        taskImplClassName = System.getProperty("x.task.impl");
        if (null == taskImplClassName) {
            taskImplClassName = "DefaultCountingTask";
        }

        CountingTask[] tasks = createTasks(taskImplClassName, N, iterations);
        Thread[] demoThreads = new Thread[N];
        for (int i = 0; i < N; i++) {
            demoThreads[i] = new FalseSharingDemo(tasks[i]);
        }
        long start = System.currentTimeMillis();
        // 启动并等待指定的线程终止
        Tools.startAndWaitTerminated(demoThreads);
        System.out
                .printf("Duration: %,d ms %n", System.currentTimeMillis() - start);
    }

    private static CountingTask[] createTasks(String taskImplClassName, int N,
                                              long iterations) {
        CountingTask[] tasks = new CountingTask[N];
        // 这里必须连续创建多个XXCountingTask实例,
        // 创建这些实例期间不能创建其他实例以提高Java虚拟机为这些对象分配连续的内存空间的几率。
        if ("DefaultCountingTask".equals(taskImplClassName)) {
            for (int i = 0; i < N; i++) {
                tasks[i] = new DefaultCountingTask(iterations);
            }
        } else if ("AutoPaddedCountingTask".equals(taskImplClassName)) {
            for (int i = 0; i < N; i++) {
                tasks[i] = new AutoPaddedCountingTask(iterations);
            }
        } else {
            for (int i = 0; i < N; i++) {
                tasks[i] = new ManuallyPaddedCountingTask(iterations);
            }
        }
        return tasks;
    }

    @Override
    public void run() {
        final CountingTask t = task;
        final long count = t.getIterations();
        for (long i = 0; i < count; i++) {
            t.setValue(t.getValue() + i);
        }
    }
}
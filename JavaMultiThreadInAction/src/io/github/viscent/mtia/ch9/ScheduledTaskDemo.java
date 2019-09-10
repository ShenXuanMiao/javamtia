package io.github.viscent.mtia.ch9;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledTaskDemo {
    static ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        final int argc = args.length;
        // 任务执行最大耗时
        int maxConsumption;
        // 任务执行最小耗时
        int minConsumption;
        if (argc >= 2) {
            minConsumption = Integer.valueOf(args[0]);
            maxConsumption = Integer.valueOf(args[1]);
        } else {
            maxConsumption = minConsumption = 1000;
        }
        ses.scheduleAtFixedRate(new SimulatedTask(minConsumption, maxConsumption,
                "scheduleAtFixedRate"), 0, 2, TimeUnit.SECONDS);
        ses.scheduleWithFixedDelay(new SimulatedTask(minConsumption,
                maxConsumption,
                "scheduleWithFixedDelay"), 0, 1, TimeUnit.SECONDS);
        Thread.sleep(20000);

        ses.shutdown();
    }

    static class SimulatedTask implements Runnable {
        // 模拟任务执行耗时
        private final int maxConsumption;
        private final int minConsumption;
        private final AtomicInteger seq = new AtomicInteger(0);
        private String name;

        public SimulatedTask(int minConsumption, int maxConsumption, String name) {
            this.maxConsumption = maxConsumption;
            this.minConsumption = minConsumption;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                // 模拟任务执行耗时
                Tools.randomPause(maxConsumption, minConsumption);
                Debug.info(name + " run-" + seq.incrementAndGet());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// run结束
    }
}
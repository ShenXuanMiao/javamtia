package io.github.viscent.mtia.ch9;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicTaskResultHandlingDemo {
    final static ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        final String host = args[0];
        final AsyncTask<Integer> asyncTask = new AsyncTask<Integer>(ses) {
            final Random rnd = new Random();
            final String targetHost = host;

            @Override
            public Integer call() throws Exception {
                return pingHost();
            }

            private Integer pingHost() throws Exception {
                // 模拟实际操作耗时
                Tools.randomPause(2000);
                // 模拟的探测结果码
                Integer r = Integer.valueOf(rnd.nextInt(4));
                return r;
            }

            @Override
            protected void onResult(Integer result) {
                // 将结果保存到数据库
                saveToDatabase(result);
            }

            private void saveToDatabase(Integer result) {
                Debug.info(targetHost + " status:" + String.valueOf(result));
                // 省略其他代码
            }

            @Override
            public String toString() {
                return "Ping " + targetHost + "," + super.toString();
            }
        };

        ses.scheduleAtFixedRate(asyncTask, 0, 3, TimeUnit.SECONDS);

        Tools.delayedAction("The ScheduledExecutorService will be shutdown", new Runnable() {
            @Override
            public void run() {
                ses.shutdown();
            }
        }, 60);
    }
}
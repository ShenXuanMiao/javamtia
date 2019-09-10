package io.github.viscent.mtia.ch3.case03;

import io.github.viscent.mtia.util.Tools;

public class CaseRunner3_3 {

    public static void main(String[] args) throws InterruptedException {
        final AlarmMgr alarmMgr = AlarmMgr.INSTANCE;
        Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < threads.length; i++) {
            // 模拟多个线程调用alarmMgr.init();
            threads[i] = new Thread() {
                @Override
                public void run() {
                    alarmMgr.init();
                }
            };
        }

        // 启动并等待指定的线程结束
        Tools.startAndWaitTerminated(threads);
    }

}

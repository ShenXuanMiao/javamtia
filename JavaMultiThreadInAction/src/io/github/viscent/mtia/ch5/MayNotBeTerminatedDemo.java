package io.github.viscent.mtia.ch5;

import io.github.viscent.mtia.util.Debug;

public class MayNotBeTerminatedDemo {
    public static void main(String[] args) throws InterruptedException {
        TaskRunner tr = new TaskRunner();
        tr.init();

        tr.submit(new Runnable() {
            @Override
            public void run() {
                Debug.info("before doing task");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // 什么也不做:这会导致线程中断标记被清除
                }
                Debug.info("after doing task");
            }
        });
        tr.workerThread.interrupt();
    }
}

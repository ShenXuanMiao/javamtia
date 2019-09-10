package io.github.viscent.mtia.ch5;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.atomic.AtomicBoolean;

//@WebListener
public class ThreadManagementContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent ctxEvt) {
        // 停止所有登记的线程
        ThreadTerminationRegistry.INSTANCE.clearThreads();// 语句①
    }

    @Override
    public void contextInitialized(ServletContextEvent ctxEvt) {
        // 创建并启动一个数据库监控线程
        AbstractMonitorThread databaseMonitorThread;
        databaseMonitorThread = new AbstractMonitorThread(
                2000) {
            @Override
            protected void doMonitor() {
                Debug.info("Monitoring database...");
                // ...

                // 模拟实际的时间消耗
                Tools.randomPause(100);
            }
        };

        databaseMonitorThread.start();
    }

    /**
     * 抽象监控线程
     *
     * @author Viscent Huang
     */
    static abstract class AbstractMonitorThread extends Thread {
        // 线程停止标记
        final AtomicBoolean terminationToken = new AtomicBoolean(false);
        // 监控周期
        private final long interval;

        public AbstractMonitorThread(long interval) {
            this.interval = interval;
            // 设置为守护线程!
            setDaemon(true);
            ThreadTerminationRegistry.Handler handler;
            handler = new ThreadTerminationRegistry.Handler() {
                @Override
                public void terminate() {
                    terminationToken.set(true);
                    AbstractMonitorThread.this.interrupt();
                }
            }; // 语句②
            ThreadTerminationRegistry.INSTANCE.register(handler); // 语句③
        }

        @Override
        public void run() {
            try {
                while (!terminationToken.get()) {
                    doMonitor();
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                // 什么也不做
            }
            Debug.info("terminated:%s", Thread.currentThread());
        }

        // 子类覆盖该方法来实现监控逻辑
        protected abstract void doMonitor();
    }
}
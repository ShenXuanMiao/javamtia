package io.github.viscent.mtia.ch5.case02;

import io.github.viscent.mtia.util.Debug;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractService implements Service {
    protected final CountDownLatch latch;
    protected boolean started = false;

    public AbstractService(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    // 留给子类实现的抽象方法，用于实现服务器的启动逻辑
    protected abstract void doStart() throws Exception;

    @Override
    public void start() {
        new ServiceStarter().start();
    }

    @Override
    public void stop() {
        // 默认什么也不做
    }

    class ServiceStarter extends Thread {
        @Override
        public void run() {
            final String serviceName = AbstractService.this.getClass()
                    .getSimpleName();
            Debug.info("Starting %s", serviceName);
            try {
                doStart();
                started = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                Debug.info("Done Starting %s", serviceName);
            }
        }
    }
}
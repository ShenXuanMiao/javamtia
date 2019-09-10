package io.github.viscent.mtia.ch8;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XThreadFactory implements ThreadFactory {
    final static Logger LOGGER = Logger.getAnonymousLogger();
    private final UncaughtExceptionHandler ueh;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    // 所创建的线程的线程名前缀
    private final String namePrefix;

    public XThreadFactory(UncaughtExceptionHandler ueh, String name) {
        this.ueh = ueh;
        this.namePrefix = name;
    }

    public XThreadFactory(String name) {
        this(new LoggingUncaughtExceptionHandler(), name);
    }

    public XThreadFactory(UncaughtExceptionHandler ueh) {
        this(ueh, "thread");
    }

    public XThreadFactory() {
        this(new LoggingUncaughtExceptionHandler(), "thread");
    }

    protected Thread doMakeThread(final Runnable r) {
        return new Thread(r) {
            @Override
            public String toString() {
                // 返回对问题定位更加有益的信息
                ThreadGroup group = getThreadGroup();
                String groupName = null == group ? "" : group.getName();
                String threadInfo = getClass().getSimpleName() + "[" + getName() + ","
                        + getId() + ","
                        + groupName + "]@" + hashCode();
                return threadInfo;
            }
        };
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = doMakeThread(r);
        t.setUncaughtExceptionHandler(ueh);
        t.setName(namePrefix + "-" + threadNumber.getAndIncrement());
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("new thread created" + t);
        }
        return t;
    }

    static class LoggingUncaughtExceptionHandler implements
            UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            // 将线程异常终止的相关信息记录到日志中
            LOGGER.log(Level.SEVERE, t + " terminated:", e);
        }
    }// LoggingUncaughtExceptionHandler类定义结束
}
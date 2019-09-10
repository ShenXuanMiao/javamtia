package io.github.viscent.mtia.ch8;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class AppListener implements ServletContextListener {
    final static Logger LOGGER = Logger.getAnonymousLogger();

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        // 设置默认UncaughtExceptionHandler
        UncaughtExceptionHandler ueh = new LoggingUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(ueh);

        // 启动若干工作者线程
        startServices();
    }

    protected void startServices() {
        // 省略其他代码
    }

    protected void stopServices() {
        // 省略其他代码
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        Thread.setDefaultUncaughtExceptionHandler(null);
        stopServices();
    }

    static class LoggingUncaughtExceptionHandler implements
            UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            String threadInfo = "Thread[" + t.getName() + "," + t.getId() + ","
                    + t.getThreadGroup().getName() + ",@" + t.hashCode() + "]";

            // 将线程异常终止的相关信息记录到日志中
            LOGGER.log(Level.SEVERE, threadInfo + " terminated:", e);
        }
    }
}
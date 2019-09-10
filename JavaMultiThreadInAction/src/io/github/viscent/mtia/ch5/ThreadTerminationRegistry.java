package io.github.viscent.mtia.ch5;

import java.util.HashSet;
import java.util.Set;

/**
 * 线程终止登记表
 *
 * @author Viscent Huang
 */
public enum ThreadTerminationRegistry {
    INSTANCE;

    private final Set<Handler> handlers = new HashSet<Handler>();

    public synchronized void register(Handler handler) {
        handlers.add(handler);
    }

    public void clearThreads() {
        // 为保障线程安全，在遍历时将handlers复制一份
        final Set<Handler> handlersSnapshot;
        synchronized (this) {
            handlersSnapshot = new HashSet<Handler>(handlers);
        }

        for (Handler handler : handlersSnapshot) {
            try {
                handler.terminate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 线程终止处理器
     * <p>
     * 封装了有关线程停止的知识
     *
     * @author Viscent Huang
     */
    public static interface Handler {
        void terminate();
    }
}

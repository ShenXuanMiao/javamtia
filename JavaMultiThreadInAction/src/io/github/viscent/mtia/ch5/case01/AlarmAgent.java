package io.github.viscent.mtia.ch5.case01;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.Random;

/**
 * 报警代理类 负责与报警服务器建立网络连接，并对外暴露一个sendAlarm方法用于将指定的报警信息上传到报警服务器上
 *该类是饿汉模式单例类 线程安全
 * @author Viscent Huang
 */
public class AlarmAgent {
    // 保存该类的唯一实例
    private final static AlarmAgent INSTANCE = new AlarmAgent();
    // 心跳线程，用于检测报警代理与报警服务器的网络连接是否正常
    private final HeartbeartThread heartbeatThread = new HeartbeartThread();
    // 是否连接上报警服务器
    private boolean connectedToServer = false;

    private AlarmAgent() {
        // 什么也不做
    }

    public static AlarmAgent getInstance() {
        return INSTANCE;
    }

    public void init() {
        connectToServer();
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }

    private void connectToServer() {
        // 创建并启动网络连接线程，在该线程中与报警服务器建立连接
        new Thread(this::doConnect).start();
    }

    private void doConnect() {
        // 模拟实际操作耗时
        Tools.randomPause(100);
        synchronized (this) {
            connectedToServer = true;
            // 连接已经建立完毕，通知以唤醒报警发送线程
            notify();
        }
    }

    public void sendAlarm(String message) throws InterruptedException {
        synchronized (this) {
            // 使当前线程等待直到报警代理与报警服务器的连接建立完毕或者恢复
            while (!connectedToServer) {
                Debug.info("Alarm agent was not connected to server.");
                wait();
            }
            // 真正将报警消息上报到报警服务器
            doSendAlarm(message);
        }
    }

    private void doSendAlarm(String message) {
        // ...
        Debug.info("Alarm sent:%s", message);
    }

    // 心跳线程
    class HeartbeartThread extends Thread {
        @Override
        public void run() {
            try {
                // 留一定的时间给网络连接线程与报警服务器建立连接
                Thread.sleep(1000);
                while (true) {
                    if (checkConnection()) {
                        connectedToServer = true;
                    } else {
                        connectedToServer = false;
                        Debug.info("Alarm agent was disconnected from server.");

                        // 检测到连接中断，重新建立连接
                        connectToServer();
                    }
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                // 什么也不做;
            }
        }

        // 检测与报警服务器的网络连接情况
        private boolean checkConnection() {
            boolean isConnected = true;
            final Random random = new Random();

            // 模拟随机性的网络断链
            int rand = random.nextInt(1000);
            if (rand <= 500) {
                isConnected = false;
            }
            return isConnected;
        }
    }
}

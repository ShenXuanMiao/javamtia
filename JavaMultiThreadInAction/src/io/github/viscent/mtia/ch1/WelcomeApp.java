package io.github.viscent.mtia.ch1;

public class WelcomeApp {

    public static void main(String[] args) {
        // 创建线程
        Thread welcomeThread = new WelcomeThread();

        // 启动线程
        welcomeThread.start();

        // 输出“当前线程”的线程名称
        System.out.printf("1.Welcome! I'm %s.%n", Thread.currentThread().getName());
    }
}

// 定义Thread类的子类
class WelcomeThread extends Thread {

    // 在该方法中实现线程的任务处理逻辑
    @Override
    public void run() {
        System.out.printf("2.Welcome! I'm %s.%n", Thread.currentThread().getName());
    }
}

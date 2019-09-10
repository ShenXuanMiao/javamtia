package io.github.viscent.mtia.ch2;

import io.github.viscent.mtia.util.Tools;

public class ThreadJoinVisibility {
    // 线程间的共享变量
    static int data = 0;

    public static void main(String[] args) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                // 使当前线程休眠R毫秒（R的值为随机数）
                Tools.randomPause(50);

                // 更新data的值
                data = 1;
            }
        };

        thread.start();

        // 等待线程thread结束后，main线程才继续运行
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 读取并打印变量data的值
        System.out.println(data);

    }
}

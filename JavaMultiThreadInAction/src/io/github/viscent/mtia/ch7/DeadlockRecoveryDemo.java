package io.github.viscent.mtia.ch7;

import io.github.viscent.mtia.ch7.diningphilosophers.DiningPhilosopherProblem;

public class DeadlockRecoveryDemo {

    public static void main(String[] args) throws Exception {
        // 创建并启动死锁检测与恢复线程
        new DeadlockDetector().start();
        // 指定RecoverablePhilosopher为哲学家模型实现类
        System.setProperty("x.philo.impl", "RecoverablePhilosopher");
        // 启动哲学家就餐问题模拟程序
        DiningPhilosopherProblem.main(args);
    }
}

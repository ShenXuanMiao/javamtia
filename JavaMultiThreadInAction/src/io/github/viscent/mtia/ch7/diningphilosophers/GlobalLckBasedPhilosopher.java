package io.github.viscent.mtia.ch7.diningphilosophers;

import io.github.viscent.mtia.util.Debug;

public class GlobalLckBasedPhilosopher extends AbstractPhilosopher {
    // GLOBAL_LOCK必须使用static修饰
    private final static Object GLOBAL_LOCK = new Object();

    public GlobalLckBasedPhilosopher(int id, Chopstick left,
                                     Chopstick right) {
        super(id, left, right);
    }

    @Override
    public void eat() {
        synchronized (GLOBAL_LOCK) {
            Debug.info("%s is picking up %s on his left...%n", this, left);
            left.pickUp();
            Debug.info("%s is picking up %s on his right...%n", this, right);
            right.pickUp();
            doEat();
            right.putDown();
            left.putDown();
        }
    }// eat方法结束
}
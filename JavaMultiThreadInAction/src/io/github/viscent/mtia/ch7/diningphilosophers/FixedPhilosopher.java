package io.github.viscent.mtia.ch7.diningphilosophers;

import io.github.viscent.mtia.util.Debug;

public class FixedPhilosopher extends AbstractPhilosopher {
    private final Chopstick one;
    private final Chopstick theOther;

    public FixedPhilosopher(int id, Chopstick left, Chopstick right) {
        super(id, left, right);
        // 对资源（锁）进行排序
        int leftHash = System.identityHashCode(left);
        int rightHash = System.identityHashCode(right);
        if (leftHash < rightHash) {
            one = left;
            theOther = right;
        } else if (leftHash > rightHash) {
            one = right;
            theOther = left;
        } else {
            // 两个对象的identityHashCode值相等是可能的，尽管这个几率很小
            one = null;
            theOther = null;
        }
    }

    @Override
    public void eat() {
        if (null != one) {
            synchronized (one) {
                Debug.info("%s is picking up %s on his %s...%n", this, one,
                        one == left ? "left" : "right");
                one.pickUp();
                synchronized (theOther) {
                    Debug.info("%s is picking up %s on his %s...%n", this,
                            theOther, theOther == left ? "left" : "right");
                    theOther.pickUp();
                    doEat();
                    theOther.putDown();
                }
                one.putDown();
            }
        } else {
            // 退化为使用粗锁法
            synchronized (FixedPhilosopher.class) {
                Debug.info("%s is picking up %s on his left...%n", this, left);
                left.pickUp();

                Debug.info("%s is picking up %s on his right...%n", this, right);
                right.pickUp();
                doEat();
                right.putDown();

                left.putDown();
            }
        }// if语句结束
    }// eat方法结束
}
package io.github.viscent.mtia.ch7.diningphilosophers;

import io.github.viscent.mtia.util.Debug;

import java.util.concurrent.locks.ReentrantLock;

public class RecoverablePhilosopher extends BuggyLckBasedPhilosopher {

    public RecoverablePhilosopher(int id, Chopstick left, Chopstick right) {
        super(id, left, right);
    }

    @Override
    protected boolean pickUpChopstick(Chopstick chopstick) {
        final ReentrantLock lock = LOCK_MAP.get(chopstick);
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            // 使当前线程释放其已持有的锁
            Debug.info("%s detected interrupt.", Thread.currentThread().getName());
            Chopstick theOtherChopstick = chopstick == left ? right : left;
            theOtherChopstick.putDown();
            LOCK_MAP.get(theOtherChopstick).unlock();
            return false;
        }
        try {
            Debug.info(
                    "%s is picking up %s on his %s...%n",
                    this, chopstick, chopstick == left ? "left" : "right");

            chopstick.pickUp();
        } catch (Exception e) {
            // 不大可能走到这里
            e.printStackTrace();
            lock.unlock();
            return false;
        }
        return true;
    }
}

package io.github.viscent.mtia.ch3;

import io.github.viscent.mtia.ch2.CircularSeqGenerator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockbasedCircularSeqGenerator implements CircularSeqGenerator {
    private final Lock lock = new ReentrantLock();
    private short sequence = -1;

    @Override
    public short nextSequence() {
        lock.lock();
        try {
            if (sequence >= 999) {
                sequence = 0;
            } else {
                sequence++;
            }
            return sequence;
        } finally {
            lock.unlock();
        }
    }
}

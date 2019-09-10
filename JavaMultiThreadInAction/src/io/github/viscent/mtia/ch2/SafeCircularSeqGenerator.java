package io.github.viscent.mtia.ch2;

public class SafeCircularSeqGenerator implements CircularSeqGenerator {
    private short sequence = -1;

    @Override
    public synchronized short nextSequence() {
        if (sequence >= 999) {
            sequence = 0;
        } else {
            sequence++;
        }
        return sequence;
    }
}
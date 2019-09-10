package io.github.viscent.mtia.ch3;

public class Counter {
    private volatile long count;

    public long vaule() {
        return count;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
            value = "VO_VOLATILE_INCREMENT",
            justification = "It is done inside critical section")
    public void increment() {
        synchronized (this) {
            count++;
        }
    }

}

package io.github.viscent.mtia.ch12;

public class AutoPaddedCountingTask implements CountingTask {
    private final long iterations;
    @sun.misc.Contended
    public volatile long value;

    public AutoPaddedCountingTask() {
        this(1000000);
    }

    public AutoPaddedCountingTask(long iterations) {
        this.iterations = iterations;
    }

    @Override
    public long getIterations() {
        return iterations;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public void setValue(long value) {
        this.value = value;
    }
}
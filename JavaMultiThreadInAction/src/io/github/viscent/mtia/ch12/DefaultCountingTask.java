package io.github.viscent.mtia.ch12;

public class DefaultCountingTask implements CountingTask {
    private final long iterations;
    private volatile long value;

    public DefaultCountingTask() {
        this(1000000);
    }

    public DefaultCountingTask(long iterations) {
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
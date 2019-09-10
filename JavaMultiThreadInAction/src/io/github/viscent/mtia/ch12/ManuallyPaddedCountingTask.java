package io.github.viscent.mtia.ch12;

public class ManuallyPaddedCountingTask implements CountingTask {
    private final long iterations;
    public volatile long value;
    // 填充
    protected volatile long p1, p2, p3, p4;

    public ManuallyPaddedCountingTask() {
        this(1000000);
    }

    public ManuallyPaddedCountingTask(long iterations) {
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
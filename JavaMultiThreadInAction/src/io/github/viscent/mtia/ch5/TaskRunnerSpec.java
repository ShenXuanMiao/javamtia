package io.github.viscent.mtia.ch5;

public interface TaskRunnerSpec {
    public void init();

    public void submit(Runnable task) throws InterruptedException;
}

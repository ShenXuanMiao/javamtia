package io.github.viscent.mtia.ch9;

import java.util.concurrent.Executor;

public class SynchronousExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
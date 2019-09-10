package io.github.viscent.mtia.ch8;

import io.github.viscent.mtia.util.Tools;

import java.util.Random;

public class TaskWithException implements Runnable {
    final Random rnd;

    public TaskWithException(Random rnd) {
        super();
        this.rnd = rnd;
    }

    @Override
    public void run() {

        while (true) {
            if (rnd.nextInt(100) < 2) {
                throw new RuntimeException("test");
            }
            Tools.randomPause(50);
        }

    }

}

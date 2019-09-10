package io.github.viscent.mtia.ch7.diningphilosophers;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

/**
 * 对哲学家进行抽象
 *
 * @author Viscent Huang
 */
public abstract class AbstractPhilosopher extends Thread implements Philosopher {
    protected final int id;
    protected final Chopstick left;
    protected final Chopstick right;

    public AbstractPhilosopher(int id, Chopstick left, Chopstick right) {
        super("Philosopher-" + id);
        this.id = id;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        for (; ; ) {
            think();
            eat();
        }
    }

    /*
     * @see io.github.viscent.mtia.ch7.diningphilosophers.Philosopher#eat()
     */
    @Override
    public abstract void eat();

    protected void doEat() {
        Debug.info("%s is eating...%n", this);
        Tools.randomPause(10);
    }

    /*
     * @see io.github.viscent.mtia.ch7.diningphilosophers.Philosopher#think()
     */
    @Override
    public void think() {
        Debug.info("%s is thinking...%n", this);
        Tools.randomPause(10);
    }

    @Override
    public String toString() {
        return "Philosopher-" + id;
    }
}
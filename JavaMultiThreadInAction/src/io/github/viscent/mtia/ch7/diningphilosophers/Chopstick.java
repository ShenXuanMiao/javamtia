package io.github.viscent.mtia.ch7.diningphilosophers;

/**
 * 筷子
 *
 * @author Viscent Huang
 */
public class Chopstick {
    public final int id;
    private Status status = Status.PUT_DOWN;

    public Chopstick(int id) {
        super();
        this.id = id;
    }

    public void pickUp() {
        status = Status.PICKED_UP;
    }

    public void putDown() {
        status = Status.PUT_DOWN;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "chopstick-" + id;
    }

    public static enum Status {
        PICKED_UP,
        PUT_DOWN
    }
}
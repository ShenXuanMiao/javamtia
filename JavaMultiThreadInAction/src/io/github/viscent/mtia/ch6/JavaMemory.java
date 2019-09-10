package io.github.viscent.mtia.ch6;

import io.github.viscent.mtia.util.Debug;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaMemory {

    public static void main(String[] args) {
        String msg = args.length > 0 ? args[0] : null;
        ObjectX objX = new ObjectX();
        objX.greet(msg);
    }
}

class ObjectX implements Serializable {
    private static final long serialVersionUID = 8554375271108416940L;
    private static AtomicInteger ID_Generator = new AtomicInteger(0);
    private Date timeCreated = new Date();
    private int id;

    public ObjectX() {
        this.id = ID_Generator.getAndIncrement();
    }

    public void greet(String message) {
        String msg = toString() + ":" + message;
        Debug.info(msg);
    }

    @Override
    public String toString() {
        return "[" + timeCreated + "] ObjectX [" + id + "]";
    }
}
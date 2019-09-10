package io.github.viscent.mtia.ch12.case01;

public class TimeoutException extends Exception {
    private static final long serialVersionUID = 1L;

    public TimeoutException(long timeOut, String extraMessage) {
        super("timeout:" + timeOut + ",additional message:\n" + extraMessage);
    }
}

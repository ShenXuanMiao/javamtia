package io.github.viscent.mtia.ch3;

public class FinalFieldExample {
    static FinalFieldExample instance;
    final int x;
    int y;

    public FinalFieldExample() {
        x = 1;
        y = 2;
    }

    public static void writer() {
        instance = new FinalFieldExample();
    }

    public static void reader() {
        final FinalFieldExample theInstance = instance;
        if (theInstance != null) {
            int diff = theInstance.y - theInstance.x;
            // diff的值可能为1(=2-1），也可能为-1（=0-1）。
            print(diff);
        }
    }

    private static void print(int x) {
        // ...
    }
}
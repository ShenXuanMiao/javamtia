package io.github.viscent.mtia.ch12;

import java.util.Random;

public class LockCoarseningExample {
    private final Random rnd = new Random();

    public void simulate() {
        int iq1 = randomIQ();
        int iq2 = randomIQ();
        int iq3 = randomIQ();
        act(iq1, iq2, iq3);
    }

    private void act(int... n) {
        // ...
    }

    /**
     * 返回随机的智商值
     *
     * @return
     */
    public int randomIQ() {
        // 人类智商的标准差是15，平均值是100
        return (int) Math.round(rnd.nextGaussian() * 15 + 100);
    }
    // ...
}

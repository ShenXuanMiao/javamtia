package io.github.viscent.mtia.ch5.case02;

import io.github.viscent.mtia.util.Tools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class SampleServiceC extends AbstractService {

    public SampleServiceC(CountDownLatch latch) {
        super(latch);
    }

    @Override
    public void doStart() throws Exception {
        // 模拟实际操作耗时
        Tools.randomPause(2000);

        // 省略其他代码

        // 模拟服务器启动失败
        final Random random = new Random();
        int rand = random.nextInt(1000);
        if (rand <= 500) {
            throw new RuntimeException("test");
        }
    }

}

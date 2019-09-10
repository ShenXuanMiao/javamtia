package io.github.viscent.mtia.ch5.case02;

import io.github.viscent.mtia.util.Tools;

import java.util.concurrent.CountDownLatch;

public class SampleServiceA extends AbstractService {

    public SampleServiceA(CountDownLatch latch) {
        super(latch);

    }

    @Override
    protected void doStart() throws Exception {
        // 模拟实际操作耗时
        Tools.randomPause(1000);

        // 省略其他代码
    }

}

package io.github.viscent.mtia.ch8;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.io.File;
import java.util.concurrent.*;

public class TaskResultRetrievalDemo {
    final static int N_CPU = Runtime.getRuntime().availableProcessors();
    final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, N_CPU * 2, 4,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        TaskResultRetrievalDemo demo = new TaskResultRetrievalDemo();
        Future<String> future = demo.recognizeImage("/tmp/images/0001.png");
        // 执行其他操作
        doSomething();
        try {
            // 仅在需要相应任务的处理结果时才调用Future.get()
            Debug.info(future.get());
        } catch (InterruptedException e) {
            // 什么也不做
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void doSomething() {
        Tools.randomPause(200);
    }

    public Future<String> recognizeImage(final String imageFile) {
        return executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return doRecognizeImage(new File(imageFile));
            }
        });
    }

    protected String doRecognizeImage(File imageFile) {
        String result = null;
        // 模拟实际运行结果
        String[] simulatedResults = {"苏Z MM518", "苏Z XYZ618", "苏Z 007618"};
        result = simulatedResults[(int) (Math.random() * simulatedResults.length)];
        Tools.randomPause(100);
        // 省略其他代码
        return result;
    }
}
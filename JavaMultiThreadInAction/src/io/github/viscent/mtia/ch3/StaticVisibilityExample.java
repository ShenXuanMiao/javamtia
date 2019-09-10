package io.github.viscent.mtia.ch3;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.HashMap;
import java.util.Map;

public class StaticVisibilityExample {
    private static Map<String, String> taskConfig;

    static {
        Debug.info("The class being initialized...");
        taskConfig = new HashMap<String, String>();// 语句①
        taskConfig.put("url", "https://github.com/Viscent");// 语句②
        taskConfig.put("timeout", "1000");// 语句③
    }

    public static void changeConfig(String url, int timeout) {
        taskConfig = new HashMap<String, String>();// 语句④
        taskConfig.put("url", url);// 语句⑤
        taskConfig.put("timeout", String.valueOf(timeout));// 语句⑥
    }

    public static void init() {
        // 该线程至少能够看到语句①～语句③的操作结果，而能否看到语句④～语句⑥的操作结果是没有保障的。
        Thread t = new Thread() {
            @Override
            public void run() {
                String url = taskConfig.get("url");
                String timeout = taskConfig.get("timeout");
                doTask(url, Integer.valueOf(timeout));
            }
        };
        t.start();
    }

    private static void doTask(String url, int timeout) {
        // 省略其他代码

        // 模拟实际操作的耗时
        Tools.randomPause(500);
    }
}
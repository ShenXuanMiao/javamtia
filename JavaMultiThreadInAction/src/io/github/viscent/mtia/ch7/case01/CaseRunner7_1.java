package io.github.viscent.mtia.ch7.case01;

import io.github.viscent.mtia.util.Debug;
import io.github.viscent.mtia.util.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * 本程序可能导致死锁
 *
 * @author Viscent Huang
 */
public class CaseRunner7_1 {

    final static ConfigurationHelper configHelper = ConfigurationHelper.INSTANCE.init();

    public static void main(String[] args) throws InterruptedException {
        // 模拟业务线程读取配置实体
        Thread trxThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Configuration cfg = configHelper.getConfig("serverInfo");
                String url = cfg.getProperty("url");
                process(url);
            }

            private void process(String url) {
                Debug.info("processing %s", url);
                // ...
            }

        });

        // 模拟系统管理线程更新配置数据
        Thread updateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // 模拟实际操作所需的时间
                Tools.randomPause(40);

                Map<String, String> props = new HashMap<String, String>();
                props.put("property1", "value1");
                props.put("property2", "value2");
                props.put("property3", "value3");
                ConfigurationManager.INSTANCE.update("anotherConfig", 6, props);
            }

        });

        // 启动并等待指定的线程终止
        Tools.startAndWaitTerminated(trxThread, updateThread);
    }
}
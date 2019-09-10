package io.github.viscent.mtia.ch3;

import io.github.viscent.mtia.util.Debug;

import java.util.Map;

public class SafeObjPublishWhenStartingThread {
    private final Map<String, String> objectState;

    private SafeObjPublishWhenStartingThread(Map<String, String> objectState) {
        this.objectState = objectState;
        // 不在构造器中启动工作者线程，以避免this逸出
    }

    // 工厂方法
    public static SafeObjPublishWhenStartingThread newInstance(
            Map<String, String> objState) {
        SafeObjPublishWhenStartingThread instance = new SafeObjPublishWhenStartingThread(
                objState);
        instance.init();
        return instance;
    }

    private void init() {
        // 创建并启动工作者线程
        new Thread() {
            @Override
            public void run() {
                // 访问外层类实例的状态变量
                String value = objectState.get("someKey");
                Debug.info(value);
                // 省略其他代码
            }
        }.start();
    }
}
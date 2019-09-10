package io.github.viscent.mtia.ch3;

import io.github.viscent.mtia.util.Debug;

public class StaticHolderSingleton {
    // 私有构造器
    private StaticHolderSingleton() {
        Debug.info("StaticHolderSingleton inited.");
    }

    public static StaticHolderSingleton getInstance() {
        Debug.info("getInstance invoked.");
        return InstanceHolder.INSTANCE;
    }

    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                Debug.info(StaticHolderSingleton.InstanceHolder.class.getName());
                StaticHolderSingleton.InstanceHolder.INSTANCE.someService();
            }

            ;
        };
        t.start();
    }

    public void someService() {
        Debug.info("someService invoked.");
        // 省略其他代码
    }

    static class InstanceHolder {
        final static StaticHolderSingleton INSTANCE = new StaticHolderSingleton();

        // 保存外部类的唯一实例
        static {
            Debug.info("InstanceHolder inited.");
        }
    }
}

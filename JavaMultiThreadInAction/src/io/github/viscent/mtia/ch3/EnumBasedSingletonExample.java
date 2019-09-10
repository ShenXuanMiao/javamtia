package io.github.viscent.mtia.ch3;

import io.github.viscent.mtia.util.Debug;

public class EnumBasedSingletonExample {
    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                Debug.info(Singleton.class.getName());
                Singleton.INSTANCE.someService();
            }

            ;
        };
        t.start();
    }

    public static enum Singleton {
        INSTANCE;

        // 私有构造器
        Singleton() {
            Debug.info("Singleton inited.");
        }

        public void someService() {
            Debug.info("someService invoked.");
            // 省略其他代码
        }
    }
}

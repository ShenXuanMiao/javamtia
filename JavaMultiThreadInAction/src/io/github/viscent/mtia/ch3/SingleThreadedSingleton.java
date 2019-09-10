package io.github.viscent.mtia.ch3;

public class SingleThreadedSingleton {
    // 保存该类的唯一实例
    private static SingleThreadedSingleton instance = null;

    // 省略实例变量声明
    /*
     * 私有构造器使其他类无法直接通过new创建该类的实例
     */
    private SingleThreadedSingleton() {
        // 什么也不做
    }

    /**
     * 创建并返回该类的唯一实例 <BR>
     * 即只有该方法被调用时该类的唯一实例才会被创建
     *
     * @return
     */
    public static SingleThreadedSingleton getInstance() {
        if (null == instance) {// 操作①
            instance = new SingleThreadedSingleton();// 操作②
        }
        return instance;
    }

    public void someService() {
        // 省略其他代码
    }
}

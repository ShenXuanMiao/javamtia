package io.github.viscent.mtia.ch3;

public class DCLSingleton {
    /*
     * 保存该类的唯一实例，使用volatile关键字修饰instance。
     */
    private static volatile DCLSingleton instance;

    /*
     * 私有构造器使其他类无法直接通过new创建该类的实例
     */
    private DCLSingleton() {
        // 什么也不做
    }

    /**
     * 创建并返回该类的唯一实例 <BR>
     * 即只有该方法被调用时该类的唯一实例才会被创建
     *
     * @return
     */
    public static DCLSingleton getInstance() {
        if (null == instance) {// 操作①：第1次检查
            synchronized (DCLSingleton.class) {
                if (null == instance) {// 操作②：第2次检查
                    instance = new DCLSingleton();// 操作③
                }
            }
        }
        return instance;
    }

    public void someService() {
        // 省略其他代码
    }
}

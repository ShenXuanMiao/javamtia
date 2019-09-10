package io.github.viscent.mtia.ch3;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * 基于双重检查锁定的错误单例模式实现
 *
 * @author Viscent Huang
 */
public class IncorrectDCLSingletion {
    // 保存该类的唯一实例
    private static IncorrectDCLSingletion instance = null;

    /*
     * 私有构造器使其他类无法直接通过new创建该类的实例
     */
    private IncorrectDCLSingletion() {
        // 什么也不做
    }

    /**
     * 创建并返回该类的唯一实例 <BR>
     * 即只有该方法被调用时该类的唯一实例才会被创建
     *
     * @return
     */
    @SuppressFBWarnings(value = "DC_DOUBLECHECK",
            justification = "此处特意使用双重检查锁定")
    public static IncorrectDCLSingletion getInstance() {
        if (null == instance) {// 操作①：第1次检查
            synchronized (IncorrectDCLSingletion.class) {
                if (null == instance) {// 操作②：第2次检查
                    instance = new IncorrectDCLSingletion();// 操作③
                }
            }
        }
        return instance;
    }

    public void someService() {
        // 省略其他代码
    }
}

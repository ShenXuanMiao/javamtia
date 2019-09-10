package io.github.viscent.mtia.ch5.case02;

public class ServerStarter {

    public static void main(String[] args) {
        // 省略其他代码

        // 启动所有服务
        ServiceManager.startServices();

        // 执行其他操作

        // 在所有其他操作执行结束后，检测服务启动状态
        boolean allIsOK;
        // 检测全部服务的启动状态
        allIsOK = ServiceManager.checkServiceStatus();

        if (allIsOK) {
            System.out.println("All services were sucessfully started!");
            // 省略其他代码
        } else {
            // 个别服务启动失败，退出JVM
            System.err.println("Some service(s) failed to start,exiting JVM...");
            System.exit(1);
        }
        // ...
    }
}

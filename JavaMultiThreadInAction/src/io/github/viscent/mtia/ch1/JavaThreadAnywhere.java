package io.github.viscent.mtia.ch1;

public class JavaThreadAnywhere {

    public static void main(String[] args) {
        // 获取当前线程
        Thread currentThread = Thread.currentThread();

        // 获取当前线程的线程名称
        String currentThreadName = currentThread.getName();

        System.out.printf("The main method was executed by thread:%s",
                currentThreadName);
        Helper helper = new Helper("Java Thread AnyWhere");
        helper.run();
    }

    static class Helper implements Runnable {
        private final String message;

        public Helper(String message) {
            this.message = message;
        }

        private void doSomething(String message) {
            // 获取当前线程
            Thread currentThread = Thread.currentThread();

            // 获取当前线程的线程名称
            String currentThreadName = currentThread.getName();

            System.out.printf("The doSomething method was executed by thread:%s",
                    currentThreadName);
            System.out.println("Do something with " + message);
        }

        @Override
        public void run() {
            doSomething(message);
        }
    }
}
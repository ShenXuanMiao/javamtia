package io.github.viscent.mtia.ch1;

import java.util.Date;

public class AJavaApp {

    public static void main(String[] args) {
        while (true) {

            System.out.println(new Date());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}

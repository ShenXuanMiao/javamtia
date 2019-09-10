package io.github.viscent.mtia.ch2;

public class ReorderingIntro {
    protected static int a, b, c, d;

    public static void main(String[] args) {
        b = a * 2;// 语句①
        c = 1;// 语句②
        d = 2;// 语句③

        System.out.println(a + "," + b + "," + c + "," + d);

    }
}

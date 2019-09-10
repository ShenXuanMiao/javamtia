package io.github.viscent.mtia.ch5.case03;

import io.github.viscent.mtia.ch4.case02.CaseRunner4_2;

public class CaseRunner5_3 {

    public static void main(String[] args) throws Exception {
        System.setProperty("x.stat.task",
                "io.github.viscent.mtia.ch5.case03.StatTask");
        CaseRunner4_2.main(args);
    }

}

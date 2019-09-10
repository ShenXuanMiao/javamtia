package io.github.viscent.mtia.ch5.case04;

import io.github.viscent.mtia.ch5.TerminatableTaskRunner;
import io.github.viscent.mtia.util.Debug;

public class CaseRunner5_4 {
    static TerminatableTaskRunner taskRunner = new TerminatableTaskRunner();

    static {
        taskRunner.init();
    }

    public static void main(String[] args) throws Exception {

        MusicFinder finder = new MusicFinder(taskRunner, "/home/viscent/",
                "/home/viscent/tmp/localmusic.lst", 2000);
        Debug.info("starting searching...");
        finder.searchMusicFiles();

        Thread.sleep(2000);
        // 执行其他任务
        taskRunner.submit(new Runnable() {
            @Override
            public void run() {
                Debug.info("Some other task");
            }
        });

        taskRunner.shutdown();
    }
}

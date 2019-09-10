package io.github.viscent.mtia.ch2;

public class SpeculativeLoadExample {
    private boolean ready = false;
    private int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

    public void writer() {
        int[] newData = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        for (int i = 0; i < newData.length; i++) {// 语句①（for循环语句）

            // 此处包含读内存的操作
            newData[i] = newData[i] - i;
        }
        data = newData;
        // 此处包含写内存的操作
        ready = true;// 语句②
    }

    public int reader() {
        int sum = 0;
        int[] snapshot;
        if (ready) {// 语句③（if语句）
            snapshot = data;
            for (int i = 0; i < snapshot.length; i++) {// 语句④（for循环语句）
                sum += snapshot[i];// 语句⑤
            }

        }
        return sum;
    }
}

package io.github.viscent.mtia.ch10;

public class Counter {
  private volatile long count;

  public long vaule() {
    return count;
  }

  public void increment() {
    // 此处特意不加锁，以便测试代码能够报告相应错误
    count++;
  }
}
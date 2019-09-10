package io.github.viscent.mtia.ch5;

/**
 * 对传输通道的抽象
 *
 * @author Viscent Huang
 */
public interface Channel<P> {
    /**
     * 往传输通道中存入一个产品
     *
     * @param product 产品
     */
    void put(P product) throws InterruptedException;

    /**
     * 从传输通道中取出一个产品
     *
     * @return 产品
     */
    P take() throws InterruptedException;
}
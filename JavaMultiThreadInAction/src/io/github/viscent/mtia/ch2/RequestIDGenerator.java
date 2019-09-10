package io.github.viscent.mtia.ch2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class RequestIDGenerator implements CircularSeqGenerator {
    /**
     * 保存该类的唯一实例
     */
    private final static RequestIDGenerator INSTANCE = new RequestIDGenerator();
    private final static short SEQ_UPPER_LIMIT = 999;
    private short sequence = -1;

    // 私有构造器
    private RequestIDGenerator() {
        // 什么也不做
    }

    /**
     * 返回该类的唯一实例
     *
     * @return
     */
    public static RequestIDGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * 生成循环递增序列号
     *
     * @return
     */
    @Override
    public short nextSequence() {
        if (sequence >= SEQ_UPPER_LIMIT) {
            sequence = 0;
        } else {
            sequence++;
        }
        return sequence;
    }

    /**
     * 生成一个新的Request ID
     *
     * @return
     */
    public String nextID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        DecimalFormat df = new DecimalFormat("000");

        // 生成请求序列号
        short sequenceNo = nextSequence();

        return "0049" + timestamp + df.format(sequenceNo);
    }
}
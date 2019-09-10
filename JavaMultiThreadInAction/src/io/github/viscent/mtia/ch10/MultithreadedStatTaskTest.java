package io.github.viscent.mtia.ch10;

import io.github.viscent.mtia.ch4.case02.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MultithreadedStatTaskTest {
    private MultithreadedStatTask mst;
    private int recordCount = 0;
    private String[] records;

    @Before
    public void setUp() throws Exception {
        records = new String[4];
        records[0] = "2016-03-30 09:33:04.644|SOAP|request|SMS|sendSms|OSG|ESB|00200000000|192.168.1.102|13612345678|136712345670";
        records[1] = "2016-03-30 09:33:04.688|SOAP|response|SMS|sendSmsRsp|ESB|OSG|00200000000|192.168.1.102|13612345678|136712345670";
        records[2] = "2016-03-30 09:33:04.732|SOAP|request|SMS|sendSms|ESB|NIG|00210000001|192.168.1.102|13612345678|136712345670";
        records[3] = "2016-03-30 09:33:04.772|SOAP|response|SMS|sendSmsRsp|NIG|ESB|00210000004|192.168.1.102|13612345678|136712345670";
        mst = createTask(10, 3, "sendSms", "*");
    }

    @After
    public void tearDown() throws Exception {
        recordCount = 0;
    }

    @Test
    public void testRun() {
        // 只关心MultithreadedStatTask本身（与多线程有关）
        mst.run();
        assertTrue(records.length == recordCount);
    }

    private MultithreadedStatTask createTask(
            int sampleInterval,
            int traceIdDiff, String expectedOperationName,
            String expectedExternalDeviceList) throws Exception {

        // Stub对象
        final AbstractLogReader logReader = new AbstractLogReader(
                new ByteArrayInputStream(new byte[]{}), 1024, 4) {
            boolean eof = false;
            RecordSet consumedBatch = new RecordSet(super.batchSize);

            @Override
            protected RecordSet getNextToFill() {
                return null;
            }

            @Override
            protected RecordSet nextBatch() {
                if (eof) {
                    return null;
                }
                for (String r : records) {
                    consumedBatch.putRecord(r);
                }
                eof = true;
                return consumedBatch;
            }

            @Override
            protected void publish(RecordSet recordBatch) {
                // 什么也不做
            }

            @Override
            public void run() {
                // 什么也不做
            }
        };

        // 返回MultithreadedStatTask的匿名子类
        return new MultithreadedStatTask(sampleInterval, new FakeProcessor()) {
            @Override
            protected AbstractLogReader createLogReader() {
                // 并不返回AbstractLogReader类的真实实现类LogReaderThread，而是一个Stub类实例
                return logReader;
            }
        };// 不使用StatProcessor的真实实现类RecordProcessor，而是使用Stub类FakeProcessor
    }// createTask结束

    // Stub类
    class FakeProcessor implements StatProcessor {
        @Override
        public void process(String record) {
            recordCount++;
        }

        @Override
        public Map<Long, DelayItem> getResult() {
            // 不关心该方法，故返回空的Map
            return Collections.emptyMap();
        }
    }// FakeProcessor结束
}
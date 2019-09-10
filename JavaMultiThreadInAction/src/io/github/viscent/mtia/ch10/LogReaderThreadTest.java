package io.github.viscent.mtia.ch10;

import io.github.viscent.mtia.ch4.case02.LogReaderThread;
import io.github.viscent.mtia.ch4.case02.RecordSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class LogReaderThreadTest {
    private LogReaderThread logReader;
    private StringBuilder sdb;

    @Before
    public void setUp() throws Exception {
        sdb = new StringBuilder();
        sdb.append("2016-03-30 09:33:04.644|SOAP|request|SMS|sendSms|OSG|ESB|00200000000|192.168.1.102|13612345678|136712345670");
        sdb.append("\n2016-03-30 09:33:04.688|SOAP|response|SMS|sendSmsRsp|ESB|OSG|00200000000|192.168.1.102|13612345678|136712345670");
        sdb.append("\n2016-03-30 09:33:04.732|SOAP|request|SMS|sendSms|ESB|NIG|00210000001|192.168.1.102|13612345678|136712345670");
        sdb.append("\n2016-03-30 09:33:04.772|SOAP|response|SMS|sendSmsRsp|NIG|ESB|00210000004|192.168.1.102|13612345678|136712345670\n");

        InputStream in = new ByteArrayInputStream(sdb.toString().getBytes("UTF-8"));
        logReader = new LogReaderThread(in, 1024, 4);
        logReader.start();
    }

    @After
    public void tearDown() throws Exception {
        logReader.interrupt();
    }

    @Test
    public void testNextBatch() {
        try {
            RecordSet rs = logReader.nextBatch();
            StringBuilder contents = new StringBuilder();
            String record;
            while (null != (record = rs.nextRecord())) {
                contents.append(record).append("\n");
            }
            assertTrue(contents.toString().equals(sdb.toString()));
        } catch (InterruptedException ignored) {
        }
    }
}

package io.github.viscent.mtia.ch4.case02;

import io.github.viscent.mtia.util.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleStatTask extends AbstractStatTask {
    private final InputStream in;

    public SimpleStatTask(InputStream in, int sampleInterval, int traceIdDiff,
                          String expectedOperationName, String expectedExternalDeviceList) {
        super(sampleInterval, traceIdDiff, expectedOperationName,
                expectedExternalDeviceList);
        this.in = in;
    }

    @Override
    protected void doCalculate() throws IOException, InterruptedException {
        String strBufferSize = System.getProperty("x.input.buffer");
        int inputBufferSize = null != strBufferSize ? Integer
                .valueOf(strBufferSize) : 8192 * 4;
        final BufferedReader logFileReader = new BufferedReader(
                new InputStreamReader(in), inputBufferSize);
        String record;
        try {
            while ((record = logFileReader.readLine()) != null) {
                // 实例变量recordProcessor是在AbstractStatTask中定义的
                recordProcessor.process(record);
            }
        } finally {
            Tools.silentClose(logFileReader);
        }
    }
}
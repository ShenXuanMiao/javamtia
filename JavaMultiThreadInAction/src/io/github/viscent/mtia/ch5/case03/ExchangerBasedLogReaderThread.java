package io.github.viscent.mtia.ch5.case03;

import io.github.viscent.mtia.ch4.case02.AbstractLogReader;
import io.github.viscent.mtia.ch4.case02.RecordSet;

import java.io.InputStream;
import java.util.concurrent.Exchanger;

public class ExchangerBasedLogReaderThread extends AbstractLogReader {
    private final Exchanger<RecordSet> exchanger;
    private volatile RecordSet nextToFill;
    private RecordSet consumedBatch;

    public ExchangerBasedLogReaderThread(InputStream in, int inputBufferSize,
                                         int batchSize) {
        super(in, inputBufferSize, batchSize);
        exchanger = new Exchanger<RecordSet>();
        nextToFill = new RecordSet(batchSize);
        consumedBatch = new RecordSet(batchSize);
    }

    @Override
    protected RecordSet getNextToFill() {
        return nextToFill;
    }

    @Override
    protected void publish(RecordSet recordSet) throws InterruptedException {
        nextToFill = exchanger.exchange(recordSet);
    }

    @Override
    protected RecordSet nextBatch() throws InterruptedException {
        consumedBatch = exchanger.exchange(consumedBatch);
        if (consumedBatch.isEmpty()) {
            consumedBatch = null;
        }
        return consumedBatch;
    }
}
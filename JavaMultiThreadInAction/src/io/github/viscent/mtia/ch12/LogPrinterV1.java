package io.github.viscent.mtia.ch12;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrinterV1 {
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyMMddHHmm");
    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");
    final static int MAX_RECORDS_PER_FILE = 10000;
    private PrintWriter pwr = null;
    private int recordsInFile = MAX_RECORDS_PER_FILE;
    private int fileSeq = 0;

    public void print(String record) {
        PrintWriter writer;
        try {
            synchronized (this) {
                writer = getPrintWriter();
                writer.println(record);
                recordsInFile++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void shutdown() {
        if (null != pwr) {
            pwr.close();
        }
    }

    private PrintWriter getPrintWriter() throws IOException {
        PrintWriter writer = null;
        if (MAX_RECORDS_PER_FILE == recordsInFile) {
            String newFileName = retrieveFileName();
            writer = new PrintWriter(newFileName);
            recordsInFile = 0;
            if (null != pwr) {
                pwr.flush();
                pwr.close();
            }
            pwr = writer;
        } else {
            writer = pwr;
        }
        return writer;
    }

    @SuppressFBWarnings(value = "STCAL_INVOKE_ON_STATIC_DATE_FORMAT_INSTANCE",
            justification = "该方法最终是通过一个同步块访问的，因此对DATE_FORMAT的访问并无线程安全问题")
    protected String retrieveFileName() {
        String fileName;
        fileName = "/home/viscent/tmp/logs/" + DATE_FORMAT.format(new Date())
                + DECIMAL_FORMAT.format(fileSeq) + ".log";
        if (++fileSeq > 99) {
            fileSeq = 0;
        }
        return fileName;
    }
}
package io.github.viscent.mtia.ch4.case02;

import io.github.viscent.mtia.util.Tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RecordProcessor implements StatProcessor {
    public static final int FIELDS_COUNT = 11;
    private static final FastTimeStampParser FAST_TIMESTAMP_PARSER = new FastTimeStampParser();
    private static final DecimalFormat df = new DecimalFormat("0000");

    private static final int INDEX_TIMESTAMP = 0;
    private static final int INDEX_TRACE_ID = 7;
    private static final int INDEX_MESSAGE_TYPE = 2;
    private static final int INDEX_OPERATION_NAME = 4;
    private static final int SRC_DEVICE = 5;
    private static final int DEST_DEVICE = 6;
    private final Map<Long, DelayItem> summaryResult;
    private final Map<String, DelayData> immediateResult;

    private final int traceIdDiff;
    private final String expectedOperationName;
    // 采样周期，单位：s
    private final int sampleInterval;
    private final String expectedExternalDeviceList;
    private String selfDevice = "ESB";
    private long currRecordDate;

    public RecordProcessor(int sampleInterval, int traceIdDiff,
                           String expectedOperationName, String expectedExternalDeviceList) {
        summaryResult = new TreeMap<Long, DelayItem>();

        this.immediateResult = new HashMap<String, DelayData>();
        this.sampleInterval = sampleInterval;
        this.traceIdDiff = traceIdDiff;
        this.expectedOperationName = expectedOperationName;
        this.expectedExternalDeviceList = expectedExternalDeviceList;
    }

    private static long parseTimeStamp(String timeStamp) {
        String[] parts = new String[2];
        Tools.split(timeStamp, parts, '.');

        long part1 = FAST_TIMESTAMP_PARSER.parseTimeStamp(parts[0]);
        String millisecond = parts[1];
        int part2 = 0;
        if (null != millisecond) {
            part2 = Integer.valueOf(millisecond);
        }

        return part1 + part2;
    }

    public void process(String[] recordParts) {
        String traceId;
        String matchingReqTraceId;
        String recordType;
        String interfaceName;
        String operationName;
        String timeStamp;
        String strRspTimeStamp;
        String strReqTimeStamp;
        DelayData delayData;

        traceId = recordParts[INDEX_TRACE_ID];
        recordType = recordParts[INDEX_MESSAGE_TYPE];
        timeStamp = recordParts[INDEX_TIMESTAMP];

        if ("response".equals(recordType)) {
            int nonSeqLen = traceId.length() - 4;
            String traceIdSeq = traceId.substring(nonSeqLen);

            // 获取这条响应记录相应的请求记录中的traceId
            matchingReqTraceId = traceId.substring(0, nonSeqLen)
                    + df.format(Integer.valueOf(traceIdSeq).intValue()
                    - Integer.valueOf(traceIdDiff).intValue());

            delayData = immediateResult.remove(matchingReqTraceId);
            if (null == delayData) {
                // 不可能到这里，除非日志记录有错误
                return;
            }

            delayData.setRspTime(timeStamp);
            strRspTimeStamp = timeStamp;
            strReqTimeStamp = delayData.getReqTime();

            // 仅在读取到表示相应的请求记录时才统计数据
            long reqTimeStamp = parseTimeStamp(strReqTimeStamp);
            long rspTimeStamp = parseTimeStamp(strRspTimeStamp);
            long delay = rspTimeStamp - reqTimeStamp;
            DelayItem delayStatData;

            if (reqTimeStamp - currRecordDate < sampleInterval * 1000) {
                delayStatData = summaryResult.get(currRecordDate);
            } else {
                currRecordDate = reqTimeStamp;
                delayStatData = new DelayItem(currRecordDate);
                delayStatData.getTotalDelay().addAndGet(delay);
                summaryResult.put(currRecordDate, delayStatData);
            }

            delayStatData.getSampleCount().incrementAndGet();
            delayStatData.getTotalDelay().addAndGet(delay);
        } else {
            // 记录请求数据
            delayData = new DelayData();
            delayData.setTraceId(traceId);
            delayData.setReqTime(timeStamp);

            interfaceName = recordParts[1];
            operationName = recordParts[INDEX_OPERATION_NAME];
            delayData.setOperationName(interfaceName + '.' + operationName);
            immediateResult.put(traceId, delayData);
        }
    }

    @Override
    public void process(String record) {
        String[] recordParts = filterRecord(record);
        if (null == recordParts || recordParts.length == 0) {
            return;
        }

        process(recordParts);
    }

    public String[] filterRecord(String record) {
        String[] recordParts = new String[FIELDS_COUNT];
        Tools.split(record, recordParts, '|');
        if (recordParts.length < 7) {
            return null;
        }

        String recordType = recordParts[INDEX_MESSAGE_TYPE];
        String operationName = recordParts[INDEX_OPERATION_NAME];
        String srcDevice = recordParts[SRC_DEVICE];
        String destDevice = recordParts[DEST_DEVICE];
        if ("response".equals(recordType)) {
            operationName = operationName.substring(0,
                    operationName.length() - "Rsp".length());
            recordParts[INDEX_OPERATION_NAME] = operationName;
        }

        if (!expectedOperationName.equals(operationName)) {
            recordParts = null;
        }

        if ("*".equals(expectedExternalDeviceList)) {
            if ("request".equals(recordType)) {
                if (!selfDevice.equals(srcDevice)) {
                    recordParts = null;
                }
            } else {
                if (!selfDevice.equals(destDevice)) {
                    recordParts = null;
                }
            }
        } else {
            if ("request".equals(recordType)) {
                // 仅考虑表示当前设备发送给指定列表中的其他设备的请求记录
                if (!(selfDevice.equals(srcDevice) && expectedExternalDeviceList
                        .contains(destDevice))) {
                    recordParts = null;
                }
            } else {
                // 仅考虑表示指定列表中的其他设备发生给读取设备的响应记录
                if (!(selfDevice.equals(destDevice) && expectedExternalDeviceList
                        .contains(srcDevice))) {
                    recordParts = null;
                }
            }
        }

        return recordParts;
    }

    @Override
    public Map<Long, DelayItem> getResult() {
        return summaryResult;
    }

    class DelayData {
        private String traceId;
        private String operationName;
        private String reqTime;
        private String rspTime;

        public DelayData() {

        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public String getReqTime() {
            return reqTime;
        }

        public void setReqTime(String reqTime) {
            this.reqTime = reqTime;
        }

        public String getRspTime() {
            return rspTime;
        }

        public void setRspTime(String rspTime) {
            this.rspTime = rspTime;
        }

        @Override
        public String toString() {
            return "DelayData [traceId=" + traceId + ", operationName="
                    + operationName + ", reqTime=" + reqTime + ", rspTime=" + rspTime
                    + "]";
        }

    }
}
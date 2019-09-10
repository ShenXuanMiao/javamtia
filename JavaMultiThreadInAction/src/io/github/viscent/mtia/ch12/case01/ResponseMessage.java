package io.github.viscent.mtia.ch12.case01;

public class ResponseMessage {
    private String requestID;
    private int resultCode;
    private long receiveTime;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    // ...

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public String toString() {
        return "Response [requestID=" + requestID + ", resultCode=" + resultCode + ", receiveTime="
                + receiveTime + "]";
    }

}

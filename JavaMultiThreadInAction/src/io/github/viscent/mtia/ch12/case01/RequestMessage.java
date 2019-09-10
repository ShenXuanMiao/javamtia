package io.github.viscent.mtia.ch12.case01;

import java.util.concurrent.atomic.AtomicLong;

public class RequestMessage {
    static AtomicLong seq = new AtomicLong(-1L);
    private ResponseMessage response;
    private String ID;
    private int messageType;
    private long requestTime;

    public RequestMessage() {
        ID = String.format("%06d", seq.incrementAndGet());
    }

    public String getID() {

        return ID;
    }

    public ResponseMessage getResponse() {
        return response;
    }

    public void setResponse(ResponseMessage response) {
        this.response = response;
    }

    // ...

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    public String toString() {
        return "Request [response=" + response + ", ID=" + ID + ", messageType=" + messageType
                + ", requestTime=" + requestTime + "]";
    }

}

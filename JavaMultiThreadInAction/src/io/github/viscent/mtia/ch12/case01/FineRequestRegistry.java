package io.github.viscent.mtia.ch12.case01;

import java.util.HashMap;
import java.util.Map;


public class FineRequestRegistry implements RequestRegistry {
    @SuppressWarnings({"rawtypes"})
    private final Map/* <String, RequestMessage> */requests = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void registerRequest(RequestMessage request) {
        String requestID = request.getID();
        requests.put(requestID, request);
    }

    @Override
    public synchronized void unregisterRequest(RequestMessage request) {
        String requestID = request.getID();
        requests.remove(requestID);
    }

    @Override
    public ResponseMessage waitForResponse(RequestMessage request, long timeOut)
            throws TimeoutException, InterruptedException {
        ResponseMessage res = null;
        long start = System.currentTimeMillis();
        long waitTime;
        long now;
        boolean isTimedout = false;
        synchronized (request) {
            while (null == (res = request.getResponse())) {
                now = System.currentTimeMillis();
                // 计算剩余等待时间
                waitTime = timeOut - (now - start);
                if (waitTime <= 0) {
                    // 等待超时退出
                    isTimedout = true;
                    break;
                }
                request.wait(waitTime);
            }// while循环结束
        }// synchronized结束
        if (isTimedout) {
            unregisterRequest(request);
            throw new TimeoutException(timeOut, request.toString());
        }
        return res;
    }

    @Override
    public void responseReceived(ResponseMessage response) {
        String requestID = response.getRequestID();
        RequestMessage request = null;
        synchronized (this) {
            request = (RequestMessage) requests.get(requestID);
            if (null == request) {
                return;
            }
            requests.remove(requestID);
        }
        synchronized (request) {
            request.setResponse(response);
            request.notify();
        }
    }
}

package com.gwt.extensions.client.remoteservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class ProxyStateHolder {
    private static ProxyStateHolder instance;
    private String previousPayload;
    private long payloadTimestamp;
    public ArrayList<AsyncCallback> duplicateCalls;
    private Object successResponse;
    private Throwable failureResponse;

    private ProxyStateHolder() {
    }

    public static ProxyStateHolder get() {
        if (instance == null)
            instance = new ProxyStateHolder();
        return instance;
    }

    public void setPreviousCall(String payload, long timestamp) {
        previousPayload = payload;
        payloadTimestamp = timestamp;
    }

    public void clearOldResponse() {
        successResponse = null;
        failureResponse = null;
        duplicateCalls = new ArrayList<AsyncCallback>(); // Clear() call is invalid here.. need new list everytime new call is made
    }

    public void setCurrentCallDetails(String currentPayload, long currentTimestamp) {
        payloadTimestamp = currentTimestamp;
        previousPayload = currentPayload;
    }

    public String getPreviousPayload() {
        return previousPayload;
    }

    public long getPayloadTimestamp() {
        return payloadTimestamp;
    }

    public void deferOrServeCallback(AsyncCallback callback) {
        if (successResponse != null)
            callback.onSuccess(successResponse);  // serve call based on previously received response
        else if (failureResponse != null)
            callback.onFailure(failureResponse);
        else
            duplicateCalls.add(callback); // defer call to wait on sent call
    }

    public void setFailureResponse(Throwable failureResponse) {
        this.failureResponse = failureResponse;
    }

    public void setSuccessResponse(Object successResponse) {
        this.successResponse = successResponse;
    }
}

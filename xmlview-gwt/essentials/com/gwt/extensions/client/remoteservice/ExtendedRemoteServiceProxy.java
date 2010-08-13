package com.gwt.extensions.client.remoteservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.gwt.common.client.Settings;
import com.gwt.extensions.client.caching.ServingAsyncCallback;

public class ExtendedRemoteServiceProxy extends RemoteServiceProxy implements HasToken {

    ProxyStateHolder state = ProxyStateHolder.get();
    private String token;
    Settings settings = Settings.get();

    protected ExtendedRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
        super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
    }

    @Override
    public String getServiceEntryPoint() {
        String point = super.getServiceEntryPoint();
        if (token != null && !token.trim().equals(""))
            point += ("/" + token);
        return point;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    protected boolean isDuplicate(String currentPayload, String methodName, long timestamp) {
        long diff = timestamp - state.getPayloadTimestamp();
        boolean isDouble = (diff < settings.getDoublePostInterval() && currentPayload.equals(state.getPreviousPayload()));
        if (isDouble && settings.logDuplicateSubmits())
            log("Duplicate for: " + methodName + " Time Diff : " + diff + " ms");
        return isDouble;
    }

    @Override
    protected <T> Request doInvoke(RequestCallbackAdapter.ResponseReader responseReader,
                                   String methodName,
                                   int invocationCount,
                                   String requestData,
                                   AsyncCallback<T> callback) {

        long timestamp = System.currentTimeMillis();
        if (settings.detectDuplicateSubmits()) {
            if (isDuplicate(requestData, methodName, timestamp)) {
                if (settings.serveDuplicateSubmits())
                    state.deferOrServeCallback(callback);
                return null;
            } else if (settings.serveDuplicateSubmits()) {
                callback = new ServingAsyncCallback(state, callback);
                state.clearOldResponse();
            }
            state.setCurrentCallDetails(requestData, timestamp);
        }
        return callSuper(responseReader, methodName, invocationCount, requestData, callback);
    }

    <T> Request callSuper(RequestCallbackAdapter.ResponseReader responseReader, String methodName, int invocationCount, String requestData, AsyncCallback<T> callback) {
        return super.<T>doInvoke(responseReader, methodName, invocationCount, requestData, callback);
    }

    void log(String log) {
        GWT.log(log);
    }
}

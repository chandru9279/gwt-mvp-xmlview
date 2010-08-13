package com.gwt.extensions.client.caching;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.extensions.client.remoteservice.ProxyStateHolder;

public class ServingAsyncCallback implements AsyncCallback {
    ProxyStateHolder stateHolder;
    private AsyncCallback callback;

    public ServingAsyncCallback(ProxyStateHolder stateHolder, AsyncCallback callback) {
        this.stateHolder = stateHolder;
        this.callback = callback;
    }

    @Override
    public void onFailure(Throwable throwable) {
        stateHolder.setFailureResponse(throwable);
        for (AsyncCallback callback : stateHolder.duplicateCalls)
            callback.onFailure(throwable);
        callback.onFailure(throwable);
    }

    @Override
    public void onSuccess(Object callbackPayload) {
        stateHolder.setSuccessResponse(callbackPayload);
        for (AsyncCallback callback : stateHolder.duplicateCalls)
            callback.onSuccess(callbackPayload);
        callback.onSuccess(callbackPayload);
    }

}

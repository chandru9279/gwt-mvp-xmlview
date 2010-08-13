package com.gwt.extensions.client.caching;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class CachingAsyncCallback implements AsyncCallback {
    private IMetadataCache cache;
    private HashMap<String, ArrayList<AsyncCallback>> deferredCalls;
    private String metadataSeviceName;
    private AsyncCallback callback;
    private long duration;

    public CachingAsyncCallback(IMetadataCache cache,
                                HashMap<String, ArrayList<AsyncCallback>> deferredCalls,
                                String metadataSeviceName,
                                AsyncCallback callback,
                                long duration) {
        this.cache = cache;
        this.deferredCalls = deferredCalls;
        this.metadataSeviceName = metadataSeviceName;
        this.callback = callback;
        this.duration = duration;
    }

    @Override
    public void onFailure(Throwable throwable) {
        failDeferredCalls(throwable);
        callback.onFailure(throwable);
    }

    @Override
    public void onSuccess(Object callbackPayload) {
        cache.put(metadataSeviceName, callbackPayload, duration);
        serveDeferredCalls(callbackPayload);
        callback.onSuccess(callbackPayload);
    }

    private void serveDeferredCalls(Object callbackPayload) {
        ArrayList<AsyncCallback> deferredCallbacks = deferredCalls.get(metadataSeviceName);
        for (AsyncCallback deferredCallback : deferredCallbacks)
            deferredCallback.onSuccess(callbackPayload);
        deferredCalls.remove(metadataSeviceName);
    }

    private void failDeferredCalls(Throwable throwable) {
        ArrayList<AsyncCallback> deferredCallbacks = deferredCalls.get(metadataSeviceName);
        for (AsyncCallback deferredCallback : deferredCallbacks)
            deferredCallback.onFailure(throwable);
        deferredCalls.remove(metadataSeviceName);
    }
}

package com.gwt.extensions.client.logging;

import com.google.gwt.core.client.GWT;
import com.gwt.extensions.client.remoteservice.HasToken;
import com.gwt.extensions.client.remoteservice.ICallListener;

public class ConsoleServiceCallsLogger implements ICallListener {
    @Override
    public void serviceMethodInvoked(String remoteEndpoint, boolean servedFromCache, HasToken proxy) {
        String log = "Service method invoked : " + remoteEndpoint;
        if (servedFromCache) log += " Served from cache";
        GWT.log(log, null);
    }
}

package com.gwt.extensions.client.remoteservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.common.client.Settings;
import com.gwt.extensions.client.caching.CachingAsyncCallback;
import com.gwt.extensions.client.caching.IMetadataCache;
import com.gwt.extensions.client.caching.MetadataCacheImpl;
import com.gwt.extensions.client.logging.ConsoleServiceCallsLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceCallArbitrer {
    private static ServiceCallArbitrer instance;
    private ArrayList<ICallListener> listeners = new ArrayList<ICallListener>();
    private HashMap<String, ArrayList<AsyncCallback>> deferredCalls = new HashMap<String, ArrayList<AsyncCallback>>();
    private IMetadataCache cache = new MetadataCacheImpl();
    private ConsoleServiceCallsLogger consoleLogger;
    private boolean cachingEnabled;

    private ServiceCallArbitrer() {
        Settings.SettingsListener listener = new Settings.SettingsListener() {
            @Override
            public void changed() {
                if (Settings.get().isConsoleCallLoggingEnabled())
                    enableConsoleCallLogging();
                else
                    disableConsoleCallLogging();
                cachingEnabled = Settings.get().isCachingEnabled();
            }
        };
        Settings.get().addListener(listener);
        listener.changed(); // read settings once on construction
    }

    public static ServiceCallArbitrer instance() {
        if (instance == null)
            instance = new ServiceCallArbitrer();
        return instance;
    }

    private void enableConsoleCallLogging() {
        if (consoleLogger == null) consoleLogger = new ConsoleServiceCallsLogger();
        if (!listeners.contains(consoleLogger))
            listeners.add(consoleLogger);
    }

    private void disableConsoleCallLogging() {
        if (consoleLogger != null && listeners.contains(consoleLogger))
            listeners.remove(consoleLogger);
    }

    public void addListener(ICallListener filter) {
        listeners.add(filter);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public AsyncCallback arbitrate(String remoteServiceInterfaceName, String remoteMethodName, AsyncCallback callback, long cacheabilityDuration, ExtendedRemoteServiceProxy proxy) {
        String remoteEndpoint = remoteServiceInterfaceName + "." + remoteMethodName;
        if (cachingEnabled && cacheabilityDuration != IMetadataCache.NOT_CACHEABLE) {
            if (isResponseCached(remoteEndpoint)) {
                notify(remoteEndpoint, true, proxy);
                callback.onSuccess(cache.get(remoteEndpoint));
                return null;
            } else if (deferredCalls.containsKey(remoteEndpoint)) {
                deferredCalls.get(remoteEndpoint).add(callback);
                return null;
            } else {
                notify(remoteEndpoint, false, proxy);
                deferredCalls.put(remoteEndpoint, new ArrayList<AsyncCallback>());
                return new CachingAsyncCallback(cache, deferredCalls, remoteEndpoint, callback, cacheabilityDuration);
            }
        } else {
            notify(remoteEndpoint, false, proxy);
            return callback;
        }
    }

    private void notify(String remoteEndpoint, boolean servedFromCache, ExtendedRemoteServiceProxy proxy) {
        for (ICallListener listener : listeners)
            listener.serviceMethodInvoked(remoteEndpoint, servedFromCache, proxy);
    }

    private boolean isResponseCached(String remoteEndpoint) {
        return cache.contains(remoteEndpoint);
    }

    private void log(String log) {
        GWT.log(log);
    }
}

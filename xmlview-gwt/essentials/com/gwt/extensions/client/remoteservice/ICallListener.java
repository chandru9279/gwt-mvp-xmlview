package com.gwt.extensions.client.remoteservice;

public interface ICallListener {
    /**
     * Called before the service goes over the net.
     *
     * @param remoteEndpoint  A string of the format : "name_of_the_interface.name_of_the_service_method".
     * @param servedFromCache True if the call will be served from the cache.
     * @param proxy           The proxy object on which a token may be set, which will be appended to the POST Url
     */
    void serviceMethodInvoked(String remoteEndpoint, boolean servedFromCache, HasToken proxy);
}

package com.gwt.extensions.client.remoteservice;

/**
 * Exposes the remote service proxy object as a container where a generic token can be set,
 * which should then get appended at the tail of the RPC post Url
 */
public interface HasToken {

    /**
     * Reads the currently set token
     */
    String getToken();

    /**
     * Set a string that should go in the Post URL
     *
     * @param token the value
     */
    void setToken(String token);
}

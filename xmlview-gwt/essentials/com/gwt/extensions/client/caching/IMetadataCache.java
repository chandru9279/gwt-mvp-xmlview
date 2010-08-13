package com.gwt.extensions.client.caching;

/**
 * Cache specification capable of storing java.lang.Objects against java.lang.Strings
 */
public interface IMetadataCache {

    long DOES_NOT_EXPIRE = -1;

    long NOT_CACHEABLE = -2;

    public void clear();

    public void removeCachedResult(String metadataServiceName);

    public Object get(String metadataServiceName);

    public void put(String metadataServiceName, Object result, long duration);

    public boolean contains(String metadataServiceName);

}

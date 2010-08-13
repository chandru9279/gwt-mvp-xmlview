package com.gwt.extensions.client.caching;

import java.util.Date;
import java.util.HashMap;

public class MetadataCacheImpl implements IMetadataCache {

    class CachedMetadata {
        Object data;
        long duration;
        long cachedAt;

        CachedMetadata(Object data, long duration) {
            this.data = data;
            this.duration = duration;
            this.cachedAt = new Date().getTime();
        }

        public Object getData() {
            return data;
        }

        public long getDuration() {
            return duration;
        }

        public boolean isValid() {
            return duration == DOES_NOT_EXPIRE || duration > new Date().getTime() - cachedAt;
        }
    }

    HashMap<String, CachedMetadata> responseCache = new HashMap<String, CachedMetadata>();

    @Override
    public void clear() {
        responseCache.clear();
    }

    @Override
    public void removeCachedResult(String metadataServiceName) {
        responseCache.remove(metadataServiceName);
    }

    @Override
    public Object get(String metadataServiceName) {
        return responseCache.get(metadataServiceName).getData();
    }

    @Override
    public void put(String metadataServiceName, Object result, long duration) {
        responseCache.put(metadataServiceName, new CachedMetadata(result, duration));
    }

    @Override
    public boolean contains(String metadataServiceName) {
        boolean containsKey = responseCache.containsKey(metadataServiceName);
        if (containsKey && responseCache.get(metadataServiceName).isValid()) {
            return true;
        } else {
            if (containsKey) removeCachedResult(metadataServiceName);
            return false;
        }
    }

}

package com.gwt.extensions.client.remoteservice;

import com.gwt.extensions.client.caching.IMetadataCache;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Flags those methods whose response will be cached
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface StaticResponse {
    /**
     * Allows specifying the duration for which the response is valid
     *
     * @return The duration in milliseconds
     */
    public long duration() default IMetadataCache.DOES_NOT_EXPIRE;
}

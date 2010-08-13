package com.gwt.common.client;

import java.util.HashSet;

public class Settings {

    /*Property determines if detection of double posts/ duplicate submits is enabled or not.*/

    private boolean detectDuplicateSubmits = true;

    public boolean detectDuplicateSubmits() {
        return detectDuplicateSubmits;
    }

    public void setDetectionOfDuplicateSubmits(boolean detectDuplicateSubmits) {
        this.detectDuplicateSubmits = detectDuplicateSubmits;
        inform();
    }

    /*Property determines if XHR double posts/ duplicate submits are handled.*/

    private boolean serveDuplicateSubmits = true;

    public boolean serveDuplicateSubmits() {
        return serveDuplicateSubmits;
    }

    public void setServingOfDuplicateSubmits(boolean serveDuplicateSubmits) {
        this.serveDuplicateSubmits = serveDuplicateSubmits;
        inform();
    }

    /*Property determines if client side caching is enabled.*/

    private boolean enableCaching = true;

    public boolean isCachingEnabled() {
        return enableCaching;
    }

    public void setCachingOfStaticResponse(boolean enableCaching) {
        this.enableCaching = enableCaching;
        inform();
    }

    /*Property says if service calls should be logged on the GWT console*/

    private boolean enableConsoleCallLogging = true;

    public boolean isConsoleCallLoggingEnabled() {
        return enableConsoleCallLogging;
    }

    public void setConsoleLoggingOfServiceCalls(boolean enableConsoleCallLogging) {
        this.enableConsoleCallLogging = enableConsoleCallLogging;
        inform();
    }

    /*Property duration for finding duplicates in XHR posts*/

    private long doublePostInterval = 1000L;

    public long getDoublePostInterval() {
        return doublePostInterval;
    }

    public void setDoublePostDetectionInterval(long doublePostInterval) {
        this.doublePostInterval = doublePostInterval;
        inform();
    }

    /*Property should duplicate XHR posts be logged? */

    private boolean logDuplicateSubmits = true;

    public boolean logDuplicateSubmits() {
        return logDuplicateSubmits;
    }

    public void setLoggingOfDuplicateSubmits(boolean logDuplicateSubmits) {
        this.logDuplicateSubmits = logDuplicateSubmits;
        inform();
    }

    /*---------------------------------------*/

    private HashSet<SettingsListener> listeners = new HashSet<SettingsListener>();
    private static Settings instance = new Settings();

    private Settings() {
    }

    public static Settings get() {
        return instance;
    }

    public void addListener(SettingsListener listener) {
        listeners.add(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void inform() {
        for (SettingsListener listener : listeners)
            listener.changed();
    }

    public interface SettingsListener {
        void changed();
    }

}

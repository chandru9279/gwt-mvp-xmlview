package com.gwt.xmlview.rebind.exceptions;

/**
 * This keeps cropping up for no good reasons
 */
public class InsaneException extends RuntimeException {
    public InsaneException(String whyWentMad) {
        super(whyWentMad);
    }
}

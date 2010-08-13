package com.gwt.xmlview.rebind;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.gwt.xmlview.rebind.exceptions.InsaneException;

import java.util.Arrays;
import java.util.Collection;

/**
 * Encapsulates tree logger, and provides handy XmlView module-specific methods
 */
public class HandyLogger {

    private final TreeLogger logger;

    public HandyLogger(TreeLogger logger) {
        this.logger = logger;
    }

    /**
     * User did something silly and will correct after seeing the exception message.
     * Always throws {@link com.gwt.xmlview.rebind.exceptions.InsaneException}.
     */
    public void complain(String message, Object... params)
            throws InsaneException {
        final String complaint = String.format(message, params);
        logger.log(TreeLogger.ERROR, complaint);
        throw new InsaneException(complaint);
    }

    /**
     * Post an error message and halt processing. This method always throws an
     * {@link com.google.gwt.core.ext.UnableToCompleteException}.
     */
    public void die(String message, Object... params)
            throws UnableToCompleteException {
        logger.log(TreeLogger.ERROR, String.format(message, params));
        throw new UnableToCompleteException();
    }

    /**
     * Shows warning logs
     */
    public void warn(String message, Object... params) {
        logger.log(TreeLogger.WARN, String.format(message, params));
    }

    public TreeLogger getTreeLogger() {
        return logger;
    }

    /**
     * Shows stuff on console. Used for debugging.
     */
    public void print(String message, Object... params) {
        logger.log(TreeLogger.INFO, String.format(message, params));
    }

    public void print(Object[] collection) {
        print(Arrays.asList(collection));
    }

    public void print(Collection collection) {
        for (Object item : collection)
            print(item.toString());
    }

    public void table(Object[]... objectArrays) {
        table(20, objectArrays);
    }

    public void table(int size, Object[]... columns) {
        for (int i = 0; i < columns[0].length; i++) {
            String row = "";
            for (Object[] objectArray : columns) {
                row += String.format("%" + size + "s |", objectArray[i]);
            }
            print(row);
        }
    }
}

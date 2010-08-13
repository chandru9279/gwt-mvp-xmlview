package com.gwt.xmlview.rebind.exceptions;

public class FieldException extends RuntimeException {
    public FieldException(String which) {
        super("Field " + which + " cannot be null");
    }
}

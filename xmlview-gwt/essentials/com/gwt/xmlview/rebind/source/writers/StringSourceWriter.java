package com.gwt.xmlview.rebind.source.writers;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringSourceWriter implements ISourceWriter {
    StringWriter stringWriter = new StringWriter();

    // A printwriter can write to a string as well :)
    com.gwt.xmlview.rebind.source.writers.SourceWriter writer = new com.gwt.xmlview.rebind.source.writers.SourceWriter(new PrintWriter(stringWriter));

    @Override
    public void indent() {
        writer.indent();
    }

    @Override
    public void line() {
        writer.line();
    }

    @Override
    public void outdent() {
        writer.outdent();
    }

    @Override
    public void w(String line) {
        writer.w(line);
    }

    @Override
    public void w(String format, Object... args) {
        writer.w(format, args);
    }

    @Override
    public String toString() {
        return stringWriter.toString();
    }
}

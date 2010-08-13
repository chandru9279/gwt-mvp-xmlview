package com.gwt.xmlview.rebind.source.writers;

import java.util.Arrays;
import java.util.HashSet;

public class MultiSourceWriter implements com.gwt.xmlview.rebind.source.writers.ISourceWriter {
    HashSet<com.gwt.xmlview.rebind.source.writers.ISourceWriter> sw = new HashSet<com.gwt.xmlview.rebind.source.writers.ISourceWriter>();

    public void addSourceWriter(com.gwt.xmlview.rebind.source.writers.ISourceWriter writer) {
        sw.add(writer);
    }

    public MultiSourceWriter(com.gwt.xmlview.rebind.source.writers.ISourceWriter... sourceWriters) {
        sw.addAll(Arrays.asList(sourceWriters));
    }

    @Override
    public void indent() {
        for (com.gwt.xmlview.rebind.source.writers.ISourceWriter iSourceWriter : sw)
            iSourceWriter.indent();
    }

    @Override
    public void line() {
        for (com.gwt.xmlview.rebind.source.writers.ISourceWriter iSourceWriter : sw)
            iSourceWriter.line();
    }

    @Override
    public void outdent() {
        for (com.gwt.xmlview.rebind.source.writers.ISourceWriter iSourceWriter : sw)
            iSourceWriter.outdent();
    }

    @Override
    public void w(String line) {
        for (com.gwt.xmlview.rebind.source.writers.ISourceWriter iSourceWriter : sw)
            iSourceWriter.w(line);
    }

    @Override
    public void w(String format, Object... args) {
        for (com.gwt.xmlview.rebind.source.writers.ISourceWriter iSourceWriter : sw)
            iSourceWriter.w(format, args);
    }
}

package com.gwt.xmlview.rebind.source.writers;

import java.io.PrintWriter;

public class SourceWriter implements ISourceWriter {

    private final PrintWriter pw;

    private int indent;

    public SourceWriter(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void indent() {
        ++indent;
    }

    @Override
    public void line() {
        pw.println();
    }

    @Override
    public void outdent() {
        if (indent == 0) {
            throw new IllegalStateException("Tried to outdent below zero");
        }
        --indent;
    }

    @Override
    public void w(String line) {
        printIndent();
        pw.println(line);
    }

    @Override
    public void w(String format, Object... args) {
        printIndent();
        pw.printf(format, args);
        pw.println();
    }

    private void printIndent() {
        for (int i = 0; i < indent; ++i) {
            pw.print("  ");
        }
    }
}

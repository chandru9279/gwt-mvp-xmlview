package com.gwt.xmlview.rebind.source.writers;

public interface ISourceWriter {
    void indent();

    void line();

    void outdent();

    void w(String line);

    void w(String format, Object... args);
}

package com.gwt.xmlview.rebind.source;

/**
 * Marks classes that represent source file entities like imports, getters, fields, constructors etc.
 */
public interface ISourceEntity {
    /**
     * Writes into the given writer the source coresponding to the entity represented
     *
     * @param writer The Source writer on which it's written
     */
    public void toSource(com.gwt.xmlview.rebind.source.writers.ISourceWriter writer);

}

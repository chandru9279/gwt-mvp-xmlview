package com.gwt.xmlview.rebind.parsing.xml;

import com.gwt.xmlview.rebind.exceptions.FieldException;
import com.gwt.xmlview.rebind.source.ISourceEntity;

import static com.gwt.xmlview.rebind.Util.blank;

public class Import implements ISourceEntity {
    private String namespaceURI;
    private String declaration;
    private String pkg;

    public Import(String namespaceURI) {
        setNamespaceURI(namespaceURI);
    }

    public void setNamespaceURI(String namespaceURI) {
        if (blank(namespaceURI)) throw new FieldException("namespaceURI");
        this.namespaceURI = namespaceURI;
        final String imp = namespaceURI.replace("urn:", "").replace(":", " ");
        this.declaration = imp + ".*;";
        this.pkg = imp.replace("import ", "");
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public String getDeclaration() {
        return declaration;
    }

    public String getPkg() {
        return pkg;
    }

    @Override
    public String toString() {
        return getDeclaration();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Import anImport = (Import) o;

        if (!namespaceURI.equals(anImport.namespaceURI)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return namespaceURI.hashCode();
    }

    @Override
    public void toSource(com.gwt.xmlview.rebind.source.writers.ISourceWriter s) {
        s.w(toString());
    }
}

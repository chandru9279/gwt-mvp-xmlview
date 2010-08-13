package com.gwt.xmlview.rebind.parsing.xml;

import com.gwt.xmlview.rebind.exceptions.FieldException;
import com.gwt.xmlview.rebind.source.ISourceEntity;
import com.gwt.xmlview.rebind.source.writers.ISourceWriter;

import static com.gwt.xmlview.rebind.Util.blank;


public class OoiField implements ISourceEntity {
    private String name;
    private String type;
    private String pkgName;
    private boolean provided = false;

    public OoiField(String name, String type, String pkgName) {
        setPkgName(pkgName);
        setName(name);
        setType(type);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPkgName() {
        return pkgName;
    }

    public boolean isProvided() {
        return provided;
    }

    public void setProvided(boolean provided) {
        this.provided = provided;
    }

    public void setPkgName(String pkgName) {
        if (blank(pkgName)) throw new FieldException("pkgName");
        this.pkgName = pkgName;
    }

    public void setName(String name) {
        if (blank(name)) throw new FieldException("name");
        this.name = name;
    }

    public void setType(String type) {
        if (blank(type)) throw new FieldException("type");
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OoiField that = (OoiField) o;

        if (!name.equals(that.name)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return type + " " + name + ";";
    }

    @Override
    public void toSource(ISourceWriter s) {
        s.w(toString());
    }
}

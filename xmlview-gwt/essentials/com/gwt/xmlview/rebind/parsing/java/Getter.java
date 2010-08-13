package com.gwt.xmlview.rebind.parsing.java;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.gwt.xmlview.rebind.exceptions.FieldException;
import com.gwt.xmlview.rebind.parsing.xml.OoiField;
import com.gwt.xmlview.rebind.source.ISourceEntity;

import static com.gwt.xmlview.rebind.Util.blank;

public class Getter implements ISourceEntity {
    private JClassType returnType;
    private String methodName;
    private String returnField;


    public Getter(JClassType returnType, String methodName, String returnField) {
        setMethodName(methodName);
        setReturnType(returnType);
        setReturnField(returnField);
    }

    public JClassType getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getReturnField() {
        return returnField;
    }

    public void setReturnType(JClassType returnType) {
        if (returnType == null) throw new FieldException("returnType");
        this.returnType = returnType;
    }

    public void setMethodName(String methodName) {
        if (blank(methodName)) throw new FieldException("methodName");
        this.methodName = methodName;
    }

    public void setReturnField(String returnField) {
        if (blank(returnField)) throw new FieldException("returnField");
        this.returnField = returnField;
    }

    public void setReturnField(OoiField ooiField) {
        setReturnField(ooiField.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Getter getter = (Getter) o;

        if (!methodName.equals(getter.methodName)) return false;
        if (returnField != null ? !returnField.equals(getter.returnField) : getter.returnField != null) return false;
        if (!returnType.equals(getter.returnType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = returnType.hashCode();
        result = 31 * result + methodName.hashCode();
        result = 31 * result + (returnField != null ? returnField.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("public %s %s(){ return %s; }", returnType.getQualifiedBinaryName(), methodName, returnField);
    }

    @Override
    public void toSource(com.gwt.xmlview.rebind.source.writers.ISourceWriter s) {
        s.w("public %s %s(){ ", returnType.getQualifiedBinaryName(), methodName);
        s.indent();
        s.w("return %s;", returnField);
        s.outdent();
        s.w("}");
    }
}

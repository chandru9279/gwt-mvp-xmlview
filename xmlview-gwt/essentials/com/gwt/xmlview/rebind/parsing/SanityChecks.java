package com.gwt.xmlview.rebind.parsing;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwt.xmlview.rebind.HandyLogger;
import com.gwt.xmlview.rebind.parsing.java.Getter;
import com.gwt.xmlview.rebind.parsing.xml.OoiField;

import java.util.HashSet;
import java.util.Iterator;

public class SanityChecks {
    private HandyLogger logger;
    private TypeOracle typeOracle;

    public SanityChecks(HandyLogger logger, TypeOracle typeOracle) {
        this.logger = logger;
        this.typeOracle = typeOracle;
    }

    public void fieldsAndGettersCheck(HashSet<OoiField> xmlFields, HashSet<Getter> interfaceFields) {
        OoiField field = null;
        for (Getter interfaceGetter : interfaceFields) {
            field = findInXmlFields(interfaceGetter.getReturnField(), xmlFields);
            if (field == null) {
                logger.table(xmlFields.toArray(), interfaceFields.toArray());
                logger.complain("Field %s defined in interface, is not present in UiXml", interfaceGetter.getReturnField());
            } else {
                JClassType fieldType = null;
                try {
                    fieldType = typeOracle.getType(field.getPkgName(), field.getType());
                } catch (NotFoundException e) {
                    logger.complain("Type of the field %s (defined in UiXml): %s.%s is not found", field.getName(), field.getPkgName(), field.getType());
                }
                assert fieldType != null;
                if (!fieldType.isAssignableTo(interfaceGetter.getReturnType())) {
                    logger.complain("Field %s with type %s in UiXml is not assignable to getter %s with return type %s in the interface",
                            field.getName(), field.getType(), interfaceGetter.getMethodName(), interfaceGetter.getReturnType());
                }
            }
        }
    }

    private OoiField findInXmlFields(String returnField, HashSet<OoiField> xmlFieldsSet) {
        final Iterator<OoiField> xmlFields = xmlFieldsSet.iterator();
        OoiField field = null;
        while (xmlFields.hasNext()) {
            field = xmlFields.next();
            if (field.getName().equals(returnField))
                break;
        }
        return field;
    }
}

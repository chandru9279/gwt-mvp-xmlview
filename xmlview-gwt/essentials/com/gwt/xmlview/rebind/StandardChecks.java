package com.gwt.xmlview.rebind;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class StandardChecks {

    TypeOracle typeOracle;
    HandyLogger logger;

    public StandardChecks(TypeOracle typeOracle, HandyLogger logger) {
        this.typeOracle = typeOracle;
        this.logger = logger;
    }

    public JClassType checkSubjectInterface(Class markerInterface, String typeName) throws UnableToCompleteException {
        JClassType subjectInterface = null;

        try {
            subjectInterface = typeOracle.getType(typeName);
        } catch (NotFoundException e) {
            logger.die("Interface %s not found", typeName);
        }

        if (subjectInterface.isInterface() == null) {
            logger.die(subjectInterface.getQualifiedSourceName() + " is not an interface");
        }

        JClassType markerItself = typeOracle.findType(markerInterface.getCanonicalName());

        if (!subjectInterface.isAssignableTo(markerItself)) {
            logger.die("You must use a subtype of " + markerInterface.getCanonicalName() + " in GWT.create(). E.g.,\n"
                    + "  interface Display extends DefinedView {}\n"
                    + "  GWT.create(Display.class);");
        }

        if (markerItself.equals(subjectInterface)) {
            logger.complain("Can't use DefinedView interface itself in GWT.create() 8-( ");
        }
        return subjectInterface;
    }
}

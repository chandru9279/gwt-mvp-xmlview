package com.gwt.definedview.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwt.xmlview.rebind.HandyLogger;

public class DefinedViewCreator {
    private HandyLogger logger;
    private TypeOracle typeOracle;
    private JClassType displayInterface;

    public DefinedViewCreator(HandyLogger logger, TypeOracle typeOracle, com.gwt.definedview.client.AbstractViewDefinition definition, JClassType displayInterface) {
        this.logger = logger;
        this.typeOracle = typeOracle;
        this.displayInterface = displayInterface;
    }

    public void create(String viewImplName) {
    }
}

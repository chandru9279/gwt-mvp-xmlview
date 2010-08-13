package com.gwt.xmlview.rebind;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwt.xmlview.client.XmlParams;
import com.gwt.xmlview.rebind.parsing.SanityChecks;
import com.gwt.xmlview.rebind.parsing.java.InterfaceParser;
import com.gwt.xmlview.rebind.parsing.xml.OoiXmlParser;
import com.gwt.xmlview.rebind.source.ViewWriter;
import com.gwt.xmlview.rebind.source.writers.SourceWriter;
import com.gwt.xmlview.rebind.source.writers.StringSourceWriter;

import java.io.PrintWriter;
import java.util.ArrayList;

public class XmlViewCreator {
    private HandyLogger logger;
    private TypeOracle typeOracle;
    private JClassType displayInterface;
    private ArrayList<String> templateFiles;
    private OoiXmlParser ooiXmlParser;
    private InterfaceParser inderfaceParser;
    private SanityChecks sanityChecks;
    private ViewWriter viewWriter;


    public XmlViewCreator(HandyLogger logger, TypeOracle typeOracle, JClassType displayInterface, ArrayList<String> templateFiles) {
        this.logger = logger;
        this.typeOracle = typeOracle;
        this.displayInterface = displayInterface;
        this.templateFiles = templateFiles;
        ooiXmlParser = new OoiXmlParser(logger);
        inderfaceParser = new InterfaceParser(logger);
        sanityChecks = new SanityChecks(logger, typeOracle);
    }

    public void create(String pkgName, String viewImplName, PrintWriter writer) throws UnableToCompleteException {
        final StringSourceWriter stringSource = new StringSourceWriter();
//        viewWriter = new ViewWriter(new MultiSourceWriter(new SourceWriter(writer), stringSource));
        viewWriter = new ViewWriter(new SourceWriter(writer));
        ooiXmlParser.parse(templateFiles.get(0));
        inderfaceParser.parse(displayInterface);
        sanityChecks.fieldsAndGettersCheck(ooiXmlParser.getUiXmlFields(), inderfaceParser.getGetters());
        final boolean paramsPresent = displayInterface.isAnnotationPresent(XmlParams.class);
        final XmlParams params = paramsPresent ? displayInterface.getAnnotation(XmlParams.class) : null;
        // Need to write getView() if there are provided fields or if we have more than one UiXml bindings
        final boolean implementViewFactory = paramsPresent && (params.provided().length > 0 || params.templates().length > 1);

        if (implementViewFactory) ooiXmlParser.setProvided(displayInterface.getAnnotation(XmlParams.class).provided());

        viewWriter.writePackage(pkgName);
        viewWriter.writeImports(ooiXmlParser.getImports());
        viewWriter.openClass(viewImplName, implementViewFactory, displayInterface.getName());
        viewWriter.writeUiFields(ooiXmlParser.getUiXmlFields());
        viewWriter.writeUiBinderInterfaces(viewImplName, templateFiles);
        viewWriter.writeConstructor(viewImplName, !implementViewFactory);
        if (implementViewFactory)
            viewWriter.implementViewFactory(ooiXmlParser.getProvidedUiXmlFields(), templateFiles.size());
        viewWriter.implementAbstractMethod(ooiXmlParser.getProvidedUiXmlFields());
        viewWriter.writeGetters(inderfaceParser.getGetters());
        viewWriter.closeClass();

//        logger.print(" \t\t\t\t\t\t -=SOURCE=- \n\n " + stringSource);
    }
}

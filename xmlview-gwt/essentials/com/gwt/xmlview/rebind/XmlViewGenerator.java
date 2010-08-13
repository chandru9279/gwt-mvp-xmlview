package com.gwt.xmlview.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwt.xmlview.client.XmlParams;
import com.gwt.xmlview.client.XmlView;

import java.io.PrintWriter;
import java.util.ArrayList;

import static com.gwt.xmlview.rebind.Util.getEnclosingType;
import static com.gwt.xmlview.rebind.Util.slashify;

public class XmlViewGenerator extends Generator {

    private static final String TEMPLATE_SUFFIX = ".ui.xml";

    @Override
    public String generate(TreeLogger treeLogger, GeneratorContext generatorContext, String typeName) throws UnableToCompleteException {
        TypeOracle typeOracle = generatorContext.getTypeOracle();
        assert (typeOracle != null);
        HandyLogger logger = new HandyLogger(treeLogger);
        JClassType displayInterface = new StandardChecks(typeOracle, logger).checkSubjectInterface(XmlView.class, typeName);

        logger.getTreeLogger().branch(TreeLogger.DEBUG,
                "Generating view class for the Display '" + displayInterface.getQualifiedSourceName() + "'", null);

        ArrayList<String> templateFiles = lookForUiXml(logger, displayInterface);

        String viewImplName = thinkUpImplName(logger, displayInterface);

        String pkgName = getEnclosingType(displayInterface).getPackage().getName();

        PrintWriter writer = generatorContext.tryCreate(logger.getTreeLogger(), pkgName, viewImplName);

        // writer will be null if file already written
        if (writer != null) {
            new XmlViewCreator(logger, typeOracle, displayInterface, templateFiles).create(pkgName, viewImplName, writer);
            generatorContext.commit(logger.getTreeLogger(), writer);
        }

        return pkgName + "." + viewImplName;
    }

    private String thinkUpImplName(HandyLogger logger, JClassType displayInterface) {
        displayInterface = getEnclosingType(displayInterface);
        final String interfaceName = displayInterface.getName();
        String name = removePresenterSuffix(interfaceName);
        if (name.isEmpty()) {
            logger.warn("Sad name for display interface : \"Presenter\"");
            name = interfaceName;
        }
        return name + "View_Gen";
    }

    private String removePresenterSuffix(String name) {
        return name.endsWith("Presenter") ? name.substring(0, name.indexOf("Presenter")) : name;
    }

    /**
     * Find the template file with path name and extension, given the Interface Type
     * Parses the annotation XmlParams if present
     *
     * @param logger        Logger for messages
     * @param interfaceType The interface thats wired up to the UI XML file
     * @return List of the template files' paths (if XmlParams absent, size is 1)
     * @throws UnableToCompleteException Happens if the names dont have extension .ui.xml
     */
    private ArrayList<String> lookForUiXml(HandyLogger logger, JClassType interfaceType) throws UnableToCompleteException {
        ArrayList<String> templatePaths = new ArrayList<String>();
        XmlParams annotation = interfaceType.getAnnotation(XmlParams.class);
        if (annotation != null && annotation.templates().length != 0) {
            for (int i = 0; i < annotation.templates().length; i++) {
                String template = annotation.templates()[i];
                if (!template.endsWith(TEMPLATE_SUFFIX)) {
                    logger.die("Template file name (\"%s\") must end with %s ", template, TEMPLATE_SUFFIX);
                }
                // If full path is not mentioned in path, use the interface path to get the template file
                String unsuffixed = template.substring(0, template.lastIndexOf(TEMPLATE_SUFFIX));
                template = unsuffixed.contains(".") ?
                        slashify(unsuffixed) + TEMPLATE_SUFFIX :
                        slashify(interfaceType.getPackage().getName()) + "/" + template;
                templatePaths.add(template);
            }
        } else {
            interfaceType = getEnclosingType(interfaceType);
            final String uiXmlName = removePresenterSuffix(interfaceType.getQualifiedSourceName()) + "View";
            templatePaths.add(slashify(uiXmlName) + TEMPLATE_SUFFIX);
        }
        assert templatePaths.size() > 0;
        return templatePaths;
    }

}

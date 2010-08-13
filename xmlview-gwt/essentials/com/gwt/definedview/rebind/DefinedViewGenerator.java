package com.gwt.definedview.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwt.definedview.client.AbstractViewDefinition;
import com.gwt.definedview.client.DefinedView;
import com.gwt.xmlview.rebind.HandyLogger;
import com.gwt.xmlview.rebind.StandardChecks;

public class DefinedViewGenerator extends Generator {
    private HandyLogger logger;
    private TypeOracle typeOracle;

    @Override
    public String generate(TreeLogger treeLogger, GeneratorContext generatorContext, String typeName) throws UnableToCompleteException {
        typeOracle = generatorContext.getTypeOracle();
        assert (typeOracle != null);
        logger = new HandyLogger(treeLogger);
        final JClassType displayInterface = new StandardChecks(typeOracle, logger).checkSubjectInterface(DefinedView.class, typeName);

        logger.getTreeLogger().branch(TreeLogger.DEBUG,
                "Generating view class for the Display '" + displayInterface.getQualifiedSourceName() + "'", null);
        com.gwt.definedview.client.AbstractViewDefinition definition = null;
        try {
            definition = lookForDefiningClass(displayInterface);
        } catch (ClassNotFoundException e) {
            logger.die("Defining class not found : %s", e);
        }
        String viewImplName = thinkUpImplName(logger, displayInterface);
        new DefinedViewCreator(logger, typeOracle, definition, displayInterface).create(viewImplName);
        return viewImplName;
    }

    private com.gwt.definedview.client.AbstractViewDefinition lookForDefiningClass(JClassType displayInterface) throws UnableToCompleteException, ClassNotFoundException {
        final JParameterizedType parameterized = displayInterface.getSuperclass().isParameterized();
        if (parameterized == null)
            logger.die("DefinedView is not parameterized with Definition types - in %s interface", displayInterface.getName());
        final JClassType[] params = parameterized.getTypeArgs();
        if (params.length != 1)
            logger.die("DefinedView is not parameterized with Definition types - in %s interface", displayInterface.getName());
        final JClassType definitionsType = params[0];

        if (definitionsType.isClass() == null) {
            logger.die(definitionsType.getQualifiedSourceName() + " is not a class");
        }

        final JClassType definitionSuperType = typeOracle.findType(AbstractViewDefinition.class.getCanonicalName());
        if (!definitionsType.isAssignableTo(definitionSuperType))
            logger.die("You must use a subtype of " + definitionSuperType.getName() + " to define your wire-up. E.g.,\n"
                    + "  class MyDefinition extends AbstractViewDefinition {\n"
                    + "  public void define(){"
                    + "  let(\"{fieldName}\").be(TextBox.class)"
                    + "  }"
                    + "}");
        if (definitionsType.equals(typeOracle.findType(AbstractViewDefinition.class.getCanonicalName())))
            logger.complain("Can't use AbstractViewDefinition class itself as your view definition 8-( ");
        final Class definingClassName = Class.forName(params[0].getQualifiedBinaryName());
        return instantiateDefiningClass(definingClassName);
    }

    private <T extends com.gwt.definedview.client.AbstractViewDefinition> com.gwt.definedview.client.AbstractViewDefinition instantiateDefiningClass(Class<T> definingClassName) throws UnableToCompleteException {
        /*try {
            Constructor constructor = definingClassName.getDeclaredConstructor(new Class[0]);
            try {
                constructor.setAccessible(true);
                final AbstractViewDefinition definition = (AbstractViewDefinition) constructor.newInstance(new Object[0]);
                return definition;
            } finally {
                constructor.setAccessible(false);
            }
        }
        catch (IllegalAccessException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        } catch (InstantiationException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        } catch (NoSuchMethodException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        } catch (InvocationTargetException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        }
        return null;*/

        try {
            return definingClassName.newInstance();
        } catch (InstantiationException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        } catch (IllegalAccessException e) {
            logger.die("Error creating module: %s caused by %s", definingClassName, e);
        }
        return null;
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
     * If the interface is defined as a nested class, use the name of the
     * enclosing type (The Presenter)
     *
     * @param interfaceType Usually the Display interface
     * @return The enclosing type if exists, else the Display interface itself
     */
    private JClassType getEnclosingType(JClassType interfaceType) {
        if (interfaceType.getEnclosingType() != null) {
            interfaceType = interfaceType.getEnclosingType();
        }
        return interfaceType;
    }

}

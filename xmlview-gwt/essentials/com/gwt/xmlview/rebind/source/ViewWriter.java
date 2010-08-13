package com.gwt.xmlview.rebind.source;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.gwt.xmlview.client.AbstractProvider;
import com.gwt.xmlview.client.AbstractView;
import com.gwt.xmlview.client.ViewFactory;
import com.gwt.xmlview.rebind.parsing.java.Getter;
import com.gwt.xmlview.rebind.parsing.xml.Import;
import com.gwt.xmlview.rebind.parsing.xml.OoiField;
import com.gwt.xmlview.rebind.source.writers.ISourceWriter;

import java.util.ArrayList;
import java.util.HashSet;

import static com.gwt.xmlview.rebind.Util.dotify;

/**
 * Highly smelly class :) - Public methods have to be called in order (in which its been written), if this class is to be useful!
 */
public class ViewWriter {
    private com.gwt.xmlview.rebind.source.writers.ISourceWriter s;
    private static final String BinderFieldName = "youEyeBinder";

    public ViewWriter(ISourceWriter sourceWriter) {
        this.s = sourceWriter;
    }

    public void writePackage(String pkgName) {
        s.w("package %s;", pkgName);
        s.line();
    }

    public void writeImports(HashSet<Import> imports) {
        s.line();
        for (Import anImport : imports)
            anImport.toSource(s);
        writeImport(UiField.class, AbstractView.class, AbstractProvider.class, ViewFactory.class, GWT.class, UiBinder.class, UiTemplate.class);
    }

    private void writeImport(Class... clazzes) {
        for (Class clazze : clazzes)
            s.w("import %s;", clazze.getName());
    }

    public void openClass(String viewImplName, boolean implementViewFactory, String interfaceName) {
        s.line();
        String appendInterface = implementViewFactory ? ", " + ViewFactory.class.getCanonicalName() : "";
        s.w("public class %s extends %s<%s> implements %s%s {", viewImplName, AbstractView.class.getSimpleName(), viewImplName, interfaceName, appendInterface);
        s.indent();
    }

    public void writeUiBinderInterfaces(String viewImplName, ArrayList<String> paths) {
        int i = 0;
        for (String path : paths)
            writeUiBinderInterface(viewImplName, path, i++);
    }

    public void writeUiBinderInterface(String viewImplName, String path, int index) {
        s.line();
        s.w("@UiTemplate(\"%s\")", dotify(path));
        String binderInterfaceName = viewImplName + "_YouEyeBinder_" + index;
        s.w("interface %s extends UiBinder<HTMLPanel, %s> { }", binderInterfaceName, viewImplName);
        s.line();
        s.w("private static %s %s_%d = GWT.create(%s.class);", binderInterfaceName, BinderFieldName, index, binderInterfaceName);
    }

    public void writeConstructor(String viewImplName, boolean makeInitWidgetCall) {
        s.line();
        s.w("public %s() {", viewImplName);
        s.indent();
        if (makeInitWidgetCall) s.w("initWidget(%s_0.createAndBindUi(this));", BinderFieldName);
        s.outdent();
        s.w("}");
    }

    public void implementViewFactory(HashSet<OoiField> providedUiFields, int numberOfBinders) {
        s.w("@Override");
        s.w("public Object getView(%s provider) {", AbstractProvider.class.getCanonicalName());
        s.indent();
        for (int i = 0; i < numberOfBinders; i++)
            s.w("__binders.add(%s_%d);", BinderFieldName, i);
        s.w("providerChecks(provider);");
        for (OoiField field : providedUiFields)
            s.w("this.%s = (%s)__providedFields.get(\"%s\");", field.getName(), field.getType(), field.getName());
        s.w("initWidget(__chosenBinder.createAndBindUi(this));");
        s.w("return this;");
        s.outdent();
        s.w("}");
    }

    public void implementAbstractMethod(HashSet<OoiField> providedUiFields) {
        s.line();
        s.w("@Override");
        s.w("public String[] getXmlParamsProvidedFields(){");
        s.indent();
        s.w("return %s;", getProvidedFieldsList(providedUiFields));
        s.outdent();
        s.w("}");
    }

    private String getProvidedFieldsList(HashSet<OoiField> providedUiFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("new String[]{");
        boolean arrayIsEmpty = true;
        for (OoiField field : providedUiFields) {
            builder.append("\"").append(field.getName()).append("\"").append(", ");
            arrayIsEmpty = false;
        }
        if (!arrayIsEmpty)
            builder.setLength(builder.length() - 2); // Strips the last comma-space, If array is empty no need to strip
        builder.append("}");
        return builder.toString();
    }

    public void writeUiFields(HashSet<OoiField> uiXmlFields) {
        s.line();
        for (OoiField field : uiXmlFields) {
            String insertProvided = field.isProvided() ? "(provided = true)" : "";
            s.w("@UiField%s", insertProvided);
            field.toSource(s);
            s.line();
        }
    }

    public void writeGetters(HashSet<Getter> getters) {
        s.line();
        for (Getter getter : getters) {
            s.w("@Override");
            getter.toSource(s);
            s.line();
        }
    }

    public void closeClass() {
        s.outdent();
        s.w("}");
    }

    @Override
    public String toString() {
        return s.toString();
    }
}

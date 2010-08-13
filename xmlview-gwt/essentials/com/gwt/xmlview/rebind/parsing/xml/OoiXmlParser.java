package com.gwt.xmlview.rebind.parsing.xml;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.uibinder.rebind.W3cDomHelper;
import com.gwt.xmlview.rebind.HandyLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class OoiXmlParser {

    private static final String BinderUri = "urn:ui:com.google.gwt.uibinder";
    private static final String HtmlPanelNameSpace = "urn:import:com.google.gwt.user.client.ui";
    private static final String StronglyNamedField = "field";

    private HashSet<OoiField> uiXmlFields;
    private HashSet<OoiField> providedUiXmlFields;
    private HashSet<Import> imports;
    private HandyLogger logger;

    public OoiXmlParser(HandyLogger logger) {
        this.logger = logger;
        uiXmlFields = new HashSet<OoiField>();
        providedUiXmlFields = new HashSet<OoiField>();
        imports = new HashSet<Import>();
        imports.add(new Import(HtmlPanelNameSpace));
    }

    public HashSet<OoiField> getUiXmlFields() {
        return uiXmlFields;
    }

    public HashSet<OoiField> getProvidedUiXmlFields() {
        return providedUiXmlFields;
    }

    public HashSet<Import> getImports() {
        return imports;
    }

    public void parse(String templateFile) throws UnableToCompleteException {
        final Document doc = getW3cDoc(templateFile);
        final ConsumingList<Node> nodesToParse = new ConsumingList<Node>();
        final ArrayList<Node> rootsChildren = getChildElements(doc.getDocumentElement());
        nodesToParse.addAll(rootsChildren);

        while (nodesToParse.hasItems())
            nodesToParse.addAll(getChildElements(nodesToParse.consumeFirst()));
    }

    private ArrayList<Node> getChildElements(Node node) {
        if (hasUiFieldAttribute(node)) {
            uiXmlFields.add(getField(node));
            imports.add(new Import(node.getNamespaceURI()));
        }
        ArrayList<Node> list = new ArrayList<Node>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
                list.add(child);
        }
        return list;
    }

    private OoiField getField(Node node) {
        final Node attributeNode = node.getAttributes().getNamedItemNS(BinderUri, StronglyNamedField);
        String name = attributeNode.getTextContent();
        String type = node.getLocalName();
        final String uri = node.getNamespaceURI();
        // If no namespace exists for node - then the node represents an Element like div, span, td etc
        String pkgName = "com.google.gwt.dom.client";
        if (uri != null)
            pkgName = new Import(uri).getPkg();
        return new OoiField(name, type, pkgName);
    }

    private boolean hasUiFieldAttribute(Node node) {
        if (node.hasAttributes())
            return node.getAttributes().getNamedItemNS(BinderUri, StronglyNamedField) != null;
        else
            return false;
    }

    private Document getW3cDoc(String templatePath) throws UnableToCompleteException {
        URL url = getClass().getClassLoader().getResource(templatePath);
        if (null == url)
            logger.die("Unable to find resource: " + templatePath);

        Document doc = null;
        try {
            doc = new W3cDomHelper(logger.getTreeLogger()).documentFor(url);
        } catch (SAXParseException e) {
            logger.die("Error parsing XML (line " + e.getLineNumber() + "): " + e.getMessage(), e);
        }
        return doc;
    }

    public void setProvided(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            OoiField field = getUiXmlField(strings[i]);
            field.setProvided(true);
            providedUiXmlFields.add(field);
        }
    }

    private OoiField getUiXmlField(String name) {
        for (OoiField uiXmlField : uiXmlFields)
            if (uiXmlField.getName().equals(name)) return uiXmlField;
        // Fields and getters are in sync, but provided list in XmlParams contains an unknown field 
        logger.complain("Field %s provided in XmlParams does not exist on UiXml", name);
        return null;
    }
}

package com.gwt.xmlview.rebind.parsing.java;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.gwt.xmlview.client.UiFieldName;
import com.gwt.xmlview.client.XmlParams;
import com.gwt.xmlview.rebind.HandyLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gwt.xmlview.rebind.Util.camelBack;

public class InterfaceParser {
    private HandyLogger logger;
    private Patterns patterns;
    private String convention = "";
    private static final String Pipe = "|";
    private HashSet<Getter> getters;
    private HashSet<JMethod> customMethods;
    private static final String NoMatch = "NoMatches_UnlikelyToBeAUserChosenFieldName";
    private static final String[] DefaultConventions = {"get{field}As...", "get{field}", "{field}"};

    public InterfaceParser(HandyLogger logger) {
        this.logger = logger;
        patterns = new Patterns();
        getters = new HashSet<Getter>();
        customMethods = new HashSet<JMethod>();
    }

    public HashSet<Getter> getGetters() {
        return getters;
    }

    public HashSet<JMethod> getCustomMethods() {
        return customMethods;
    }

    public void parse(JClassType displayInterface) {
        String[] conventions = DefaultConventions;
        XmlParams xmlParams = displayInterface.getAnnotation(XmlParams.class);
        if (xmlParams != null)
            conventions = xmlParams.conventions();
        final String regex = constructRegex(conventions);
        final JMethod[] methods = displayInterface.getMethods();
        final String[] extractedFieldNames = new String[displayInterface.getMethods().length];
        for (int i = 0; i < methods.length; i++) {
            JMethod method = methods[i];
            String fieldName = null;
            if (method.isAnnotationPresent(UiFieldName.class))
                fieldName = method.getAnnotation(UiFieldName.class).value();
            else
                fieldName = extractFieldName(regex, method.getName());
            if (fieldName.equals(NoMatch))
                customMethods.add(method);
            else
                getters.add(new Getter((JClassType) method.getReturnType(), method.getName(), fieldName));
        }
    }

    protected String constructRegex(String[] conventions) {
        for (int i = 0; i < conventions.length; i++)
            convention += (conventions[i] + Pipe);
        // Clean the last pipe
        convention = convention.substring(0, convention.length() - 1);
        return patterns.applyAll(convention);
    }

    /**
     * Scenario No Not Null Groups = There's a convention that doesnt have groups, and that's what matches input
     * Scenario No match = Convention specified in {@link XmlParams} is not followed :(
     * Scenario Many not null groups = Many groupings in the matching regex
     *
     * @param input The method names in the display interface
     * @param regex The RegEx against which the name is to be matched
     * @return The Field Name
     */
    private String extractFieldName(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> groups = new ArrayList<String>();
        final boolean matches = matcher.find();
        if (matches) {
            for (int i = 1; i <= matcher.groupCount(); i++)
                if (matcher.group(i) != null)
                    groups.add(matcher.group(i));
        } else {
            logger.warn("Method : %s does not match any of the given conventions {%s}.", input, convention);
            return NoMatch;
        }
        if (groups.isEmpty()) {
            logger.print("Group count : %d", matcher.groupCount());
            logger.complain("You have a convention/regex that does not have a grouping. The first valid group is considered as the field name.");
        }
        if (groups.size() > 1) {
            logger.warn("Not null group count : %d", groups.size());
            logger.warn("That's odd.. There are many groupings in the matching regex", matcher.groupCount());
        }
        return camelBack(groups.get(0));
    }
}

package com.gwt.xmlview.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;

public class Util {

    public static String slashify(String s) {
        return s.replace(".", "/");
    }

    public static String dotify(String s) {
        return s.replace("/", ".");
    }

    public static String camelBack(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
    }

    public static boolean blank(String s) {
        return s == null || s.trim().equals("");
    }

    /**
     * If the interface is defined as a nested class, get the
     * enclosing type (The Presenter)
     *
     * @param interfaceType Usually the Display interface
     * @return The enclosing type if exists, else the Display interface itself
     */
    static JClassType getEnclosingType(JClassType interfaceType) {
        if (isNestedType(interfaceType)) {
            interfaceType = interfaceType.getEnclosingType();
        }
        return interfaceType;
    }

    /**
     * Tells if a given tpye is nested within some outer calss or not
     *
     * @param interfaceType The type
     * @return True if nested.
     */
    static boolean isNestedType(JClassType interfaceType) {
        return interfaceType.getEnclosingType() != null;
    }
}

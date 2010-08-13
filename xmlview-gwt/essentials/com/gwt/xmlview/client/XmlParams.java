package com.gwt.xmlview.client;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface XmlParams {

    // Gwt bug : JAnnotationMethod.getDefaultValue() for conventions() method
    // returns (Object, which is) a 0-length JTypeParameter array :( instead of a String[]

    /**
     * Convenience constant for accessing/printing the default conventions
     * JAVAC Problem : Cannot have contants defined in annotations xD
     */
    //    String[] defaultConventions = {"get{field}As...", "get{field}", "{field}"};
    public String[] templates() default {};

    /**
     * The charsequence {field} gets replaced by (\w*)
     * The charsequence ; gets replaced by |
     * The charsequence "..." can only be at the end and ignores and matches all characters
     *
     * @return the entered pattern
     */
    public String[] conventions() default {"get{field}As...", "get{field}", "{field}"};

    /**
     * Informs the view creator that some fields will be provided.
     *
     * @return provided
     */
    public String[] provided() default {};
}

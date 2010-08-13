package com.gwt.xmlview.client;

import java.util.HashMap;

public abstract class AbstractProvider {

    protected HashMap<String, Object> provisions = new HashMap<String, Object>();
    protected int template = 1;

    public HashMap<String, Object> getProvisions() {
        return provisions;
    }

    public int getTemplate() {
        return template;
    }

    /**
     * This method should not be called from any user code.
     * It's called only once from generated code.
     */
    public abstract void define();

    public Letter provide(String fieldName) {
        return new Letter(fieldName);
    }

    /**
     * Allows choosing a template ui.xml file to render at runtime
     *
     * @param whichTemplate Index is <b>1-based</b>
     * @return The Provider
     */
    public AbstractProvider choose(int whichTemplate) {
        if (whichTemplate > 0)
            this.template = whichTemplate;
        return this;
    }

    public class Letter {
        private String name;

        public Letter(String name) {
            this.name = name;
        }

        public void with(Object instance) {
            provisions.put(name, instance);
        }
    }
}

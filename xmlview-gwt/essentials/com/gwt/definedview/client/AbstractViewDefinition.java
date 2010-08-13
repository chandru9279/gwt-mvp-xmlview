package com.gwt.definedview.client;

import java.util.HashMap;

public abstract class AbstractViewDefinition {

    private HashMap<String, Class> defenitions = new HashMap<String, Class>();

    public HashMap<String, Class> getDefenitions() {
        return defenitions;
    }

    public abstract void define();

    protected Letter let(String name) {
        return new Letter(name);
    }

    private class Letter {
        private String name;

        public Letter(String name) {
            this.name = name;
        }

        public void be(Class clazz) {
            defenitions.put(name, clazz);
        }
    }
}

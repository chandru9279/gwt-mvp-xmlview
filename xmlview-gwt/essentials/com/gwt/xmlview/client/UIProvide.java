package com.gwt.xmlview.client;

public class UIProvide {

    public static AbstractProvider choose(String template) {
        return new AbstractProvider() {
            @Override
            public void define() {
                choose(template);
            }
        };
    }

    public static Letter forFields(String... strings) {
        return new Letter(strings);
    }

    public static class Letter extends AbstractProvider {
        private String[] strings;

        public Letter(String[] strings) {
            this.strings = strings;
        }

        public AbstractProvider with(Object... objects) {
            if (objects.length != strings.length)
                throw new RuntimeException("Number fieldNames (" + strings.length + ") and objects (" + objects.length + ") dont match");
            for (int i = 0; i < objects.length; i++)
                provisions.put(strings[i], objects[i]);
            return this;
        }

        @Override
        public void define() {
        }
    }
}

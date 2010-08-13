package com.gwt.xmlview.rebind.parsing.java;

public class Patterns {
    private Rule[] rules;

    public Patterns() {
        rules = new Rule[2];
        rules[0] = new Rule("{field}", "(\\w*)");
        rules[1] = new Rule("...", ".*");
    }

    public String applyAll(String subject) {
        for (int i = 0; i < rules.length; i++) {
            subject = rules[i].apply(subject);
        }
        return subject;
    }

    class Rule {
        private final String flag;
        private final String regex;

        Rule(String flag, String regex) {
            this.flag = flag;
            this.regex = regex;
        }

        public String apply(String subject) {
            return subject.replace(flag, regex);
        }
    }
}

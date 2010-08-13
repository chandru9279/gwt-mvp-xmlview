package com.gwt.xmlview.client;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwt.xmlview.client.exceptions.ProvisionsException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Supertype of all generated views. Variable names start with double underscores
 * so that the don't clash with user chosen names
 *
 * @param <T> It's the type of the child (View) Each child has a compatible parant :)
 */
public abstract class AbstractView<T extends AbstractView> extends Composite {

    protected HashMap<String, Object> __providedFields;
    protected ArrayList<UiBinder<HTMLPanel, T>> __binders = new ArrayList<UiBinder<HTMLPanel, T>>();
    protected UiBinder<HTMLPanel, T> __chosenBinder;

    protected void providerChecks(AbstractProvider provider) {
        provider.define();

        if (provider.getTemplate() > __binders.size())
            throw new ProvisionsException("Choice was " + provider.getTemplate() + ", but only "
                    + __binders.size() + " templates are specified in XmlParams.templates()");

        __chosenBinder = __binders.get(provider.getTemplate() - 1);
        __providedFields = provider.getProvisions();

        if (__providedFields.size() > getXmlParamsProvidedFields().length)
            throw new ProvisionsException("More fields provided than defined in XmlParams. Did you miss out a field or two in XmlParams.provided() ?");
        if (__providedFields.size() < getXmlParamsProvidedFields().length)
            throw new ProvisionsException("Less fields provided than defined in XmlParams. Provide for all fields specified in XmlParams");

        for (int i = 0; i < getXmlParamsProvidedFields().length; i++) {
            String field = getXmlParamsProvidedFields()[i];
            if (!__providedFields.containsKey(field))
                throw new ProvisionsException("No provision for XmlParams.provided() field : " + field);
        }
    }

    public abstract String[] getXmlParamsProvidedFields();

}

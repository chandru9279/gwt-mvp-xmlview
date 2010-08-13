package com.gwt.xmlview.client;

/**
 * Wrong name - this interface is implemented by the View itself.
 */
public interface ViewFactory {

    /**
     * This methods simply UIProvides the view, and makes initWidget call,
     * and returns the view itself
     *
     * @param provider A provider defining fields which are instantiated at runtime
     * @return The view - A {@link com.google.gwt.user.client.ui.Composite} widget, implementing its Display interface
     */
    Object getView(AbstractProvider provider);
}

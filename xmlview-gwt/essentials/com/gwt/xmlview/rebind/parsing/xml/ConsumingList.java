package com.gwt.xmlview.rebind.parsing.xml;

import java.util.ArrayList;

/**
 * Simple list that consumes read items
 *
 * @param <T>
 */
public class ConsumingList<T> extends ArrayList<T> {

    public T consumeFirst() {
        return remove(0);
    }

    public boolean hasItems() {
        return size() != 0;
    }

}

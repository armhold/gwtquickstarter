package com.gwtquickstarter.client.widgets;

import java.util.*;
import com.google.gwt.user.client.ui.ListBox;

/**
 * a genericized ListBox whose selected item may be specified before the list items have been set
 *
 * NB: there must not be duplicates in the display (getDisplayValueForItem) value of the items.
 *
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class GenericListBox<T> extends ListBox
{
    protected List<T> items = Collections.emptyList();
    protected String selectedItemId;

    protected abstract String getIdForItem(T item);
    protected abstract String getDisplayValueForItem(T item);

    public void setItems(List<T> items)
    {
        this.items = items;

        clear();

        // pre-select the first item, if none specified
        int indexToSelect = 0;
        for (int i = 0; i < items.size(); i++)
        {
            addItem(getDisplayValueForItem(items.get(i)));

            if (getIdForItem(items.get(i)).equals(selectedItemId))
            {
                indexToSelect = i;
            }
        }

        if (items.size() > 0)
        {
            setSelectedIndex(indexToSelect);
            selectedItemId = getIdForItem(items.get(indexToSelect));
        }
    }

    public T getSelectedItem()
    {
        return getSelectedIndex() > -1 ? items.get(getSelectedIndex()) : null;
    }

    public String getSelectedItemId()
    {
        return getSelectedItem() != null ? getIdForItem(getSelectedItem()) : null;
    }

    public void setSelectedItemById(String id)
    {
        selectedItemId = id;

        for (int i = 0; i < items.size(); i++)
        {
            if (getIdForItem(items.get(i)).equals(selectedItemId))
            {
                setSelectedIndex(i);
                return;
            }
        }
    }

}

package com.gwtquickstarter.client;

import java.util.ArrayList;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.*;

/**
 * watches for History change events and updates all DOM &lt;a&gt; links with
 * "currentLink" style as appropriate for the current history token.
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class CurrentAnchorUpdater implements ValueChangeHandler<String>
{
    protected Element root;
    protected ArrayList<String> anchorIds = new ArrayList<String>();

    public CurrentAnchorUpdater(Element root)
    {
        this.root = root;
        History.addValueChangeHandler(this);
        parseAnchors(root, anchorIds);
        updateAnchors(History.getToken());
    }

    @Override
    public void onValueChange(ValueChangeEvent valueChangeEvent)
    {
        String newPlace = (String) valueChangeEvent.getValue();
        updateAnchors(newPlace);
    }

    /**
     * Find all child elements that are anchor tags, assign a unique id to them, and return a list of
     * the unique ids back.
     */
    private native void parseAnchors(Element elt, ArrayList<String> result) /*-{
      var links = elt.getElementsByTagName("a");

      for (var i = 0; i < links.length; i++ ) {
        var link = links.item(i);
        link.id = ("uid-a-" + i);
        result.@java.util.ArrayList::add(Ljava/lang/Object;) (link.id);
      }
    }-*/;


    /**
     * update all anchors found in root: add or remove "currentLink" class
     */
    protected void updateAnchors(String currentHistoryToken)
    {
        for (String anchorId : anchorIds)
        {
            Element e = DOM.getElementById(anchorId);

            // in case it was later removed from the DOM
            //
            if (e != null)
            {
                if (isCurrentLink(e, currentHistoryToken))
                {
                    e.addClassName("currentLink");
                } else
                {
                    e.removeClassName("currentLink");
                }
            }
        }
    }

    protected boolean isCurrentLink(Element e, String currentHistoryToken)
    {
        String href = e.getPropertyString("href");

        return  href != null &&
                href.contains("#") &&
                href.length() > href.lastIndexOf("#") + 1 &&
                href.substring(href.lastIndexOf("#") + 1).equals(currentHistoryToken);
    }

}

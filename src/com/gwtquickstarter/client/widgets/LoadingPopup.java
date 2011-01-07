package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.Resources;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoadingPopup extends CenteredPopup
{
    protected Resources resources = GWT.create(Resources.class);

    protected HorizontalPanel h = new HorizontalPanel();

    public LoadingPopup(String loadingMessage) {
        addStyleName("loadingPopup");

        setModal(true);
        Image img = new Image(resources.ajaxLoading());
        Label l = new Label(loadingMessage);

        h.addStyleName("loadingPopup");
        h.setSpacing(10);
        h.add(l);
        h.add(img);
        setWidget(h);
    }

}

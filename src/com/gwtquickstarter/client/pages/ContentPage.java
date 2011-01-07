package com.gwtquickstarter.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ContentPage extends Composite
{
    interface ContentPageUiBinder extends UiBinder<HTMLPanel, ContentPage>
    {
    }

    private static ContentPageUiBinder ourUiBinder = GWT.create(ContentPageUiBinder.class);

    @UiField
    VerticalPanel vpanel;

    public ContentPage(Widget content)
    {
        initWidget(ourUiBinder.createAndBindUi(this));
        vpanel.add(content);
    }

}

package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class EditEmailWidget extends Composite implements ClickHandler
{
    interface EditEmailWidgetUiBinder extends UiBinder<HTMLPanel, EditEmailWidget>
    {
    }

    private static EditEmailWidgetUiBinder uiBinder = GWT.create(EditEmailWidgetUiBinder.class);

    @UiField
    TextBox emailAddressField;

    @UiField
    Button saveChangesButton;

    @UiField
    HTMLPanel successMessage;

    @UiField
    HTML errors;

    public EditEmailWidget()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void onClick(ClickEvent event)
    {

    }

}

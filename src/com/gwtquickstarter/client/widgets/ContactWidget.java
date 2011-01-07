package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.util.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ContactWidget extends Composite implements ClickHandler
{
    interface ContactWidgetUiBinder extends UiBinder<HTMLPanel, ContactWidget>
    {
    }

    private StoreServiceAsync service = GWT.create(StoreService.class);

    private static ContactWidgetUiBinder ourUiBinder = GWT.create(ContactWidgetUiBinder.class);

    @UiField
    EnterTextBox emailField;

    @UiField
    Button submitButton;

    @UiField
    HTMLPanel thanksMessage;

    @UiField
    HTMLPanel invalidEmailMessage;
    
    public ContactWidget()
    {
        initWidget(ourUiBinder.createAndBindUi(this));
        emailField.setSubmitButton(submitButton);
        submitButton.addClickHandler(this);
    }

    public void resetAndFocus()
    {
        emailField.setText("");
        emailField.setFocus(true);
        invalidEmailMessage.setVisible(false);
    }

    @Override
    public void onClick(ClickEvent event)
    {
        final String email = emailField.getText();

        if (ClientUtils.emailIsValid(email))
        {
            invalidEmailMessage.setVisible(false);

            new AsyncCall<Void>("Saving changes...") {
                @Override
                protected void callService(AsyncCallback<Void> cb)
                {
                    service.addToMailingList(email, cb);
                }

                @Override
                public void onSuccess(Void result)
                {
                    thanksMessage.setVisible(true);
                    emailField.setText("");
                }
            }.go();
        }
        else
        {
            invalidEmailMessage.setVisible(true);
            thanksMessage.setVisible(false);
        }

        emailField.setFocus(true);
    }


}

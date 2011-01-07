package com.gwtquickstarter.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.util.*;


/**
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ForgotPasswordPage extends Page
{
    interface MyUiBinder extends UiBinder<HTMLPanel, ForgotPasswordPage>
    {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    TextBox emailField;

    @UiField
    Button resetButton;

    @UiField
    Label errorsLabel;

    @UiField
    Label successLabel;

    public ForgotPasswordPage()
    {
        super("Quick Brown Frog - Reset Password");
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("resetButton")
    public void onClick(ClickEvent event)
    {
        final String email = emailField.getText();

        if (ClientUtils.emailIsValid(email))
        {
            errorsLabel.setText("");
            errorsLabel.setVisible(false);
            resetPassword(email);
        }
        else
        {
            errorsLabel.setText("that's not a valid email address");
            errorsLabel.setVisible(true);
        }
    }


    private void resetPassword(final String email)
    {
        new AsyncCall<Void>()
        {
            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                ClientFactory.INSTANCE.getStoreService().resetPassword(email, cb);
            }

            @Override
            public void onSuccess(Void result)
            {
                successLabel.setVisible(true);
                successLabel.setText("An email has been sent to " + email + " with instructions for resetting your password");
            }

            @Override
            public void onFailure(Throwable t)
            {
                super.onFailure(t);
                errorsLabel.setText(t.getMessage());
                errorsLabel.setVisible(true);
            }
        }.go();
    }


    public static class ForgotPasswordPagePlace extends BasePlace
    {
    }

    @Prefix("!forgot")
    public static class Tokenizer implements PlaceTokenizer<ForgotPasswordPagePlace>
    {
        @Override
        public ForgotPasswordPagePlace getPlace(String token)
        {
            return new ForgotPasswordPagePlace();
        }

        @Override
        public String getToken(ForgotPasswordPagePlace place)
        {
            return "";
        }
    }

}

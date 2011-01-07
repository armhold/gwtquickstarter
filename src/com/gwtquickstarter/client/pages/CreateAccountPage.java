package com.gwtquickstarter.client.pages;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.util.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class CreateAccountPage extends Page implements ClickHandler
{
    private static final StoreServiceAsync service = GWT.create(StoreService.class);


    interface CreateAccountPageUiBinder extends UiBinder<HTMLPanel, CreateAccountPage>
    {
    }

    private static CreateAccountPageUiBinder ourUiBinder = GWT.create(CreateAccountPageUiBinder.class);

    @UiField
    TextBox usernameField;

    @UiField
    PasswordTextBox passwordField;

    @UiField
    Button createAccountButton;

    @UiField
    PasswordTextBox verifyPasswordField;

    @UiField
    HTML errors;

    @UiField
    HTMLPanel successMessage;

    @UiField
    Label createdUsernameField;

    @UiField
    HTMLPanel createAccountForm;

    @UiField
    TextBox signupKeyField;

    @UiField
    HTMLPanel alreadyHaveAccount;

    public CreateAccountPage()
    {
        super("Quick Brown Frog - create your account");
        initWidget(ourUiBinder.createAndBindUi(this));
        createAccountButton.addClickHandler(this);
    }

    @Override
    public String getMetaDescription()
    {
        return "Quick Brown Frog - create account";
    }

    @Override
    public String getByLine()
    {
        return "Create your account- this won't take long!";
    }

    @Override
    public boolean requiresHttps()
    {
        return true;
    }

    public void setSignupKeyField(String receipt)
    {
        if (! ClientUtils.isEmpty(receipt))
        {
            signupKeyField.setText(receipt);
            signupKeyField.setEnabled(false);
        }
        else
        {
            signupKeyField.setEnabled(true);
        }
    }

    private boolean fieldsAreValid()
    {
        errors.setHTML("&nbsp;");

        if (ClientUtils.isEmpty(signupKeyField.getText()))
        {
            errors.setText("missing receipt");
            return false;
        }

        if (! ClientUtils.emailIsValid(usernameField.getText()))
        {
            errors.setText("invalid email address");
            return false;
        }

        if (passwordField.getText().length() < 6)
        {
            errors.setText("password must be at least 6 characters");
            return false;
        }

        if (! ClientUtils.same(passwordField.getText(), verifyPasswordField.getText()))
        {
            errors.setText("passwords do not match");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(ClickEvent event)
    {
        if (! fieldsAreValid()) return;

        new AsyncCall<Void>("Saving changes...") {
            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                service.createAccount(signupKeyField.getText(), usernameField.getText(), passwordField.getText(), cb);
            }

            @Override
            public void onSuccess(Void result)
            {
                createdUsernameField.setText(usernameField.getText());
                successMessage.setVisible(true);
                createAccountForm.setVisible(false);
                alreadyHaveAccount.setVisible(false);
                ClientFactory.INSTANCE.getLoginSupport().login(usernameField.getText(), passwordField.getText(), false);
            }

            @Override
            public void onFailure(Throwable e)
            {
                super.onFailure(e);
                errors.setText(e.getMessage());
            }
        }.go();
    }

    public Activity createActivity(Place place)
    {
        return new DefaultPageActivity(this, place) {
            @Override
            public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
            {
                super.start(containerWidget, eventBus);
                ((CreateAccountPage) page).setSignupKeyField(((CreateAccountPlace) place).getAccountReceipt());
                usernameField.setFocus(true);
            }
        };
    }

    public static class CreateAccountPlace extends BasePlace
    {
        private String accountReceipt;

        public CreateAccountPlace(String accountReceipt)
        {
            this.accountReceipt = accountReceipt;
        }

        public String getAccountReceipt()
        {
            return accountReceipt;
        }

    }

    @Prefix("!createAccount")
    public static class Tokenizer implements PlaceTokenizer<CreateAccountPlace>
    {
        @Override
        public String getToken(CreateAccountPlace place)
        {
            return place.getAccountReceipt();
        }

        @Override
        public CreateAccountPlace getPlace(String token)
        {
            return new CreateAccountPlace(token);
        }
    }

}

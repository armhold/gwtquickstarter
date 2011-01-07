package com.gwtquickstarter.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.BasePlace;
import com.gwtquickstarter.client.ClientFactory;
import com.gwtquickstarter.client.DefaultPageActivity;
import com.gwtquickstarter.client.StoreServiceAsync;
import com.gwtquickstarter.client.util.AsyncCall;
import com.gwtquickstarter.client.util.ClientUtils;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ChangePasswordPage extends Page
{
    private static final StoreServiceAsync service = ClientFactory.INSTANCE.getStoreService();


    interface ChangePasswordPageUiBinder extends UiBinder<HTMLPanel, ChangePasswordPage>
    {
    }

    private static ChangePasswordPageUiBinder ourUiBinder = GWT.create(ChangePasswordPageUiBinder.class);

    @UiField
    PasswordTextBox verifyPasswordField;

    @UiField
    HTML errors;

    @UiField
    PasswordTextBox currentPasswordField;

    @UiField
    PasswordTextBox newPasswordField;

    @UiField
    HTMLPanel changePasswordForm;

    @UiField
    Button submitButton;

    @UiField
    HTMLPanel successMessage;

    public ChangePasswordPage()
    {
        super("Change Password");
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public boolean requiresHttps()
    {
        return true;
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

    private boolean fieldsAreValid()
    {
        errors.setHTML("&nbsp;");

        if (ClientUtils.isEmpty(currentPasswordField.getText()))
        {
            errors.setText("missing current password");
            return false;
        }

        if (newPasswordField.getText().length() < 6)
        {
            errors.setText("new password must be at least 6 characters");
            return false;
        }

        if (! ClientUtils.same(newPasswordField.getText(), verifyPasswordField.getText()))
        {
            errors.setText("passwords do not match");
            return false;
        }

        return true;
    }

    @UiHandler("submitButton")
    public void onClick(ClickEvent event)
    {
        if (! fieldsAreValid()) return;

        new AsyncCall<Void>("Saving changes...") {
            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                service.changePassword(currentPasswordField.getText(), newPasswordField.getText(), cb);
            }

            @Override
            public void onSuccess(Void result)
            {
                successMessage.setVisible(true);
                submitButton.setEnabled(false);
            }

            @Override
            public void onFailure(Throwable e)
            {
                super.onFailure(e);
                errors.setText(e.getMessage());
            }
        }.go();
    }

    public static class ChangePasswordActivity extends DefaultPageActivity
    {
        public ChangePasswordActivity(ChangePasswordPage page, ChangePasswordPage.ChangePasswordPlace place)
        {
            super(page, place);
        }

        @Override
        public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
        {
            super.start(containerWidget, eventBus);
        }
    }

    public static class ChangePasswordPlace extends BasePlace
    {
        private String accountReceipt;

        public ChangePasswordPlace(String accountReceipt)
        {
            this.accountReceipt = accountReceipt;
        }

        public String getAccountReceipt()
        {
            return accountReceipt;
        }

    }

    @Prefix("!changePassword")
    public static class Tokenizer implements PlaceTokenizer<ChangePasswordPlace>
    {
        @Override
        public String getToken(ChangePasswordPlace place)
        {
            return place.getAccountReceipt();
        }

        @Override
        public ChangePasswordPlace getPlace(String token)
        {
            return new ChangePasswordPlace(token);
        }
    }

}

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
 * @author armhold
 */
public class ChangeEmailPage extends Page
{
    private static final StoreServiceAsync service = ClientFactory.INSTANCE.getStoreService();


    interface ChangeEmailPageUiBinder extends UiBinder<HTMLPanel, ChangeEmailPage>
    {
    }

    private static ChangeEmailPageUiBinder ourUiBinder = GWT.create(ChangeEmailPageUiBinder.class);

    @UiField
    TextBox emailField;

    @UiField
    TextBox verifyEmailField;

    @UiField
    HTML errors;

    @UiField
    HTMLPanel changeEmailForm;

    @UiField
    Button submitButton;

    @UiField
    HTMLPanel successMessage;

    public ChangeEmailPage()
    {
        super("Change Email");
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

    private boolean fieldsAreValid()
    {
        errors.setHTML("&nbsp;");

        if (ClientUtils.isEmpty(emailField.getText()))
        {
            errors.setText("missing email");
            return false;
        }

        if (! ClientUtils.emailIsValid(emailField.getText()))
        {
            errors.setText("invalid email");
            return false;
        }

        if (! ClientUtils.same(emailField.getText(), verifyEmailField.getText()))
        {
            errors.setText("emails do not match");
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
                service.changeEmail(emailField.getText(), cb);
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

    public static class ChangeEmailActivity extends DefaultPageActivity
    {
        public ChangeEmailActivity(ChangeEmailPage page, ChangeEmailPage.ChangeEmailPlace place)
        {
            super(page, place);
        }

        @Override
        public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
        {
            super.start(containerWidget, eventBus);
        }
    }

    public static class ChangeEmailPlace extends BasePlace
    {
        private String accountReceipt;

        public ChangeEmailPlace(String accountReceipt)
        {
            this.accountReceipt = accountReceipt;
        }

        public String getAccountReceipt()
        {
            return accountReceipt;
        }

    }

    @Prefix("!changeEmail")
    public static class Tokenizer implements PlaceTokenizer<ChangeEmailPlace>
    {
        @Override
        public String getToken(ChangeEmailPlace place)
        {
            return place.getAccountReceipt();
        }

        @Override
        public ChangeEmailPlace getPlace(String token)
        {
            return new ChangeEmailPlace(token);
        }
    }

}

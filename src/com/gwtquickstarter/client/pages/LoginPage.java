package com.gwtquickstarter.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.events.*;
import com.gwtquickstarter.client.model.LoginResult;
import com.gwtquickstarter.client.util.*;
import com.gwtquickstarter.client.widgets.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoginPage extends Page implements ClickHandler, LoginFailureEvent.Handler, LoggedInEvent.Handler
{
    private LoginSupport loginSupport = new LoginSupport();

    interface LoginPageUiBinder extends UiBinder<HTMLPanel, LoginPage>
    {
    }

    private static LoginPageUiBinder ourUiBinder = GWT.create(LoginPageUiBinder.class);

    @UiField
    HTMLPanel loginForm;

    @UiField
    TextBox usernameField;

    @UiField
    ErrorMessage errors;

    @UiField
    EnterPasswordBox passwordField;

    @UiField
    Button loginButton;

    @UiField
    CheckBox rememberMe;

    public LoginPage()
    {
        super("Quick Brown Frog - Login");
        initWidget(ourUiBinder.createAndBindUi(this));

        ClientFactory.INSTANCE.getEventBus().addHandler(LoginFailureEvent.TYPE, this);
        ClientFactory.INSTANCE.getEventBus().addHandler(LoggedInEvent.TYPE, this);

        passwordField.setSubmitButton(loginButton);
        loginButton.addClickHandler(this);
    }

    @Override
    public String getByLine()
    {
        return "No more hunt-and-peck";
    }

    @Override
    public String getMetaDescription()
    {
        return "Login to your Quick Brown Frog account";
    }

    @Override
    public String getMetaKeywords()
    {
        return "quick brown frog, login, signup";
    }


    public void setUsernameField(String username)
    {
        usernameField.setText(username);
    }

    @Override
    public void onClick(ClickEvent event)
    {
        loginSupport.login(usernameField.getText(), passwordField.getText(), rememberMe.getValue());
    }

    @Override
    public void onLogin(LoginResult user)
    {
        errors.hide();
        passwordField.setText("");
    }


    @Override
    public void onLoginFailure(String error)
    {
        errors.showError(error);
        passwordField.setText("");
    }

    @Override
    public boolean requiresHttps()
    {
        return true;
    }

    public static class LoginPlace extends BasePlace
    {
        private String username = "";

        public LoginPlace()
        {
        }

        public LoginPlace(String username)
        {
            this.username = username;
        }

        public String getUsername()
        {
            return username;
        }
    }

    public LoginActivity createActivity(LoginPlace place)
    {
        return new LoginActivity(this, place);
    }

    public class LoginActivity extends DefaultPageActivity
    {
        public LoginActivity(LoginPage page, LoginPlace place)
        {
            super(page, place);
        }

        @Override
        public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
        {
            super.start(containerWidget, eventBus);
            ((LoginPage) page).setUsernameField(((LoginPlace) place).getUsername());
            usernameField.setFocus(true);
        }
    }

    @Prefix("!login")
    public static class Tokenizer implements PlaceTokenizer<LoginPage.LoginPlace>
    {
        @Override
        public String getToken(LoginPlace place)
        {
            return place.getUsername();
        }

        @Override
        public LoginPlace getPlace(String username)
        {
            return new LoginPlace(username);
        }
    }

}

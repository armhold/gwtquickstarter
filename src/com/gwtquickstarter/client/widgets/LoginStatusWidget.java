package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.ClientFactory;
import com.gwtquickstarter.client.events.*;
import com.gwtquickstarter.client.model.LoginResult;
import com.gwtquickstarter.client.pages.LoginPage;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoginStatusWidget extends Composite implements LoggedInEvent.Handler, LoggedOutEvent.Handler, CookieLoginFailureEvent.Handler
{
    interface LoginStatusWidgetUiBinder extends UiBinder<HTMLPanel, LoginStatusWidget>
    {
    }

    private static LoginStatusWidgetUiBinder ourUiBinder = GWT.create(LoginStatusWidgetUiBinder.class);

    @UiField
    ClickableLabel loginLink;

    @UiField
    ClickableLabel logoutLink;

    @UiField
    Label signedOnAs;

    public LoginStatusWidget()
    {
        initWidget(ourUiBinder.createAndBindUi(this));
        ClientFactory.INSTANCE.getEventBus().addHandler(LoggedInEvent.TYPE, this);
        ClientFactory.INSTANCE.getEventBus().addHandler(LoggedOutEvent.TYPE, this);
        ClientFactory.INSTANCE.getEventBus().addHandler(CookieLoginFailureEvent.TYPE, this);

        logoutLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                ClientFactory.INSTANCE.getLoginSupport().logout();
                ClientFactory.INSTANCE.getPlaceController().goTo(new LoginPage.LoginPlace());
            }
        });

        loginLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                ClientFactory.INSTANCE.getLoginSupport().attemptLoginViaCookie();
            }
        });
        
        updateDisplay();
    }


    protected void updateDisplay()
    {
        boolean signedOn = ClientFactory.INSTANCE.getLoggedInUser() != null;

        if (signedOn)
        {
            String s = ClientFactory.INSTANCE.getLoggedInUser().getUsername();
            if (ClientFactory.INSTANCE.getLoggedInUser().isAdmin())
            {
                s += " [admin]";
            }

            signedOnAs.setText(s);
        }

        signedOnAs.setVisible(signedOn);
        logoutLink.setVisible(signedOn);
        loginLink.setVisible(! signedOn);
    }

    @Override
    public void onLogin(LoginResult user)
    {
        updateDisplay();
    }

    @Override
    public void onLogout()
    {
        updateDisplay();
    }

    @Override
    public void onCookieLoginFailure(String error)
    {
        ClientFactory.INSTANCE.getPlaceController().goTo(new LoginPage.LoginPlace());
    }
}

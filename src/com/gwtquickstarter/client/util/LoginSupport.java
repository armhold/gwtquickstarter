package com.gwtquickstarter.client.util;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.events.*;
import com.gwtquickstarter.client.model.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoginSupport
{
    private static final StoreServiceAsync service = GWT.create(StoreService.class);

    public void login(final String username, final String password, final boolean rememberMe)
    {
        new AsyncCall<LoginResult>("Logging in...") {
            @Override
            protected void callService(AsyncCallback<LoginResult> cb)
            {
                service.login(username, password, rememberMe, cb);
            }

            @Override
            public void onSuccess(LoginResult user)
            {
                onSuccessfulLogin(user);
            }

            @Override
            public void onFailure(Throwable e)
            {
                ClientFactory.INSTANCE.getEventBus().fireEvent(new LoginFailureEvent(e.getMessage()));
            }

        }.go();
    }

    public void logout()
    {
        clearLoginCookie();
        ClientFactory.INSTANCE.setLoggedInUser(null);

        new AsyncCall<Void>("Logging out...") {
            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                service.logout(cb);
            }

            @Override
            public void onSuccess(Void result)
            {
                ClientFactory.INSTANCE.getEventBus().fireEvent(new LoggedOutEvent());
            }
        }.go();
    }

    public void attemptLoginViaCookie()
    {
        final String cookieSessionId = Cookies.getCookie(LoginResult.COOKIE_ID);

        if (ClientUtils.isEmpty(cookieSessionId)) {
            ClientFactory.INSTANCE.getEventBus().fireEvent(new CookieLoginFailureEvent("no cookie session"));
        }

        new AsyncCall<LoginResult>("Logging in...") {
            @Override
            protected void callService(AsyncCallback<LoginResult> cb)
            {
                service.loginViaCookie(cookieSessionId, cb);
            }

            @Override
            public void onSuccess(LoginResult user)
            {
                onSuccessfulLogin(user);
            }

            @Override
            public void onFailure(Throwable e)
            {
                ClientFactory.INSTANCE.getEventBus().fireEvent(new CookieLoginFailureEvent(e.getMessage()));
            }
        }.go();

    }

    protected void onSuccessfulLogin(LoginResult user)
    {
        ClientFactory.INSTANCE.setLoggedInUser(user);
        ClientFactory.INSTANCE.getEventBus().fireEvent(new LoggedInEvent(user));
        setLoginCookie(user.getCookieContents());

        ClientFactory.INSTANCE.getPlaceController().goTo(ClientFactory.INSTANCE.getPostLoginPlace());
        // TODO: reset postLoginPlace to some reasonable default after successful login
    }

    protected void clearLoginCookie()
    {
        // because Cookies.removeCookie() does not seem to work
        setLoginCookie("");
        Cookies.removeCookie(LoginResult.COOKIE_ID);
    }

    protected void setLoginCookie(String cookieContents)
    {
        long DURATION = 1000 * 60 * 60 * 24 * 14; // 2 weeks
        Date expires = new Date(System.currentTimeMillis() + DURATION);
        Cookies.setCookie(LoginResult.COOKIE_ID, cookieContents, expires, null, "/", false);
    }

}

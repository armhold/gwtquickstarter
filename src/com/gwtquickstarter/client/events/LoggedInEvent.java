package com.gwtquickstarter.client.events;

import com.gwtquickstarter.client.model.LoginResult;
import com.google.gwt.event.shared.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoggedInEvent extends GwtEvent<LoggedInEvent.Handler>
{
    public static Type<LoggedInEvent.Handler> TYPE = new Type<LoggedInEvent.Handler>();

    private final LoginResult user;

    public LoggedInEvent(LoginResult user)
    {
        this.user = user;
    }

    @Override
    public Type<LoggedInEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onLogin(user);
    }

    public static interface Handler extends EventHandler
    {
        public void onLogin(LoginResult user);
    }

}

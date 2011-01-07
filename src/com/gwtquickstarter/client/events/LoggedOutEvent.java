package com.gwtquickstarter.client.events;

import com.google.gwt.event.shared.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoggedOutEvent extends GwtEvent<LoggedOutEvent.Handler>
{
    public static Type<LoggedOutEvent.Handler> TYPE = new Type<LoggedOutEvent.Handler>();


    public LoggedOutEvent()
    {
    }

    @Override
    public Type<LoggedOutEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onLogout();
    }

    public static interface Handler extends EventHandler
    {
        public void onLogout();
    }

}

package com.gwtquickstarter.client.events;

import com.google.gwt.event.shared.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class CookieLoginFailureEvent extends GwtEvent<CookieLoginFailureEvent.Handler>
{
    public static Type<CookieLoginFailureEvent.Handler> TYPE = new Type<CookieLoginFailureEvent.Handler>();

    private final String error;

    public CookieLoginFailureEvent(String error)
    {
        this.error = error;
    }

    @Override
    public Type<CookieLoginFailureEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onCookieLoginFailure(error);
    }

    public static interface Handler extends EventHandler
    {
        public void onCookieLoginFailure(String error);
    }

}

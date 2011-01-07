package com.gwtquickstarter.client.events;

import com.google.gwt.event.shared.*;

/**
 * @author armhold
 */
public class LoginFailureEvent extends GwtEvent<LoginFailureEvent.Handler>
{
    public static Type<LoginFailureEvent.Handler> TYPE = new Type<LoginFailureEvent.Handler>();

    private final String error;

    public LoginFailureEvent(String error)
    {
        this.error = error;
    }

    @Override
    public Type<LoginFailureEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onLoginFailure(error);
    }

    public static interface Handler extends EventHandler
    {
        public void onLoginFailure(String error);
    }

}

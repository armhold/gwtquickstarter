package com.gwtquickstarter.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtquickstarter.client.widgets.LoadingPopup;

/**
 * A handy wrapper around AsyncCallback that allows us to pop up a "loading" dialog when
 * RPC calls take longer than a specified duration.
 *
 * See http://stackoverflow.com/questions/1309436/automatic-loading-indicator-when-calling-an-async-function
 *
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class AsyncCall<T> implements AsyncCallback<T>
{
    public static final int POPUP_DELAY_MILLIS = 1000;
    protected int delay = POPUP_DELAY_MILLIS;
    protected LoadingPopup loadingPopup;

    protected abstract void callService(AsyncCallback<T> cb);
    protected Timer timer;

    public AsyncCall()
    {
        this("Loading");
    }

    public AsyncCall(String loadingMessage, int delay)
    {
        loadingPopup = new LoadingPopup(loadingMessage);
        this.delay = delay;
    }

    public AsyncCall(String loadingMessage)
    {
        loadingPopup = new LoadingPopup(loadingMessage);
    }

    public void go()
    {
        timer  = new Timer()
        {
            public void run()
            {
                loadingPopup.show();
            }
        };

        timer.schedule(delay);
        execute();
    }

    private void execute()
    {
        callService(new AsyncCallback<T>()
        {
            public void onFailure(Throwable t)
            {
                loadingPopup.hide();
                timer.cancel();
                AsyncCall.this.onFailure(t);
            }

            public void onSuccess(T result)
            {
                loadingPopup.hide();
                timer.cancel();
                AsyncCall.this.onSuccess(result);
            }
        });
    }

    public void onFailure(Throwable t)
    {
        if (t instanceof NotLoggedInException)
        {
            GWT.log("Oops: " + t.getMessage());
        }
        else
        {
            GWT.log("Oops: " + t.getMessage(), t);
        }
    }

}

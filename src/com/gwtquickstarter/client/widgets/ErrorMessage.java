package com.gwtquickstarter.client.widgets;

import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ErrorMessage extends InlineLabel
{
    public ErrorMessage()
    {
        addStyleName("error");
        addStyleName("hidden");
    }

    public void showError(String error)
    {
        setText(error);
        show();
    }

    public void show()
    {
        removeStyleName("hidden");
    }

    public void hide()
    {
        addStyleName("hidden");
    }

}

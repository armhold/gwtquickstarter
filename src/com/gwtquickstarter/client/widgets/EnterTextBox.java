package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * A simple TextBox extension that allows user to "submit" by pressing enter.
 * This allows us to simulate behavior that users expect- hitting enter submits the "form".
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class EnterTextBox extends TextBox implements KeyPressHandler, KeyDownHandler
{
    private Button submitButton;

    public EnterTextBox()
    {
        addKeyPressHandler(this);
        addKeyDownHandler(this);
        setFocus(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event)
    {
        if (event.getNativeEvent().getCharCode() == KeyCodes.KEY_ENTER)
        {
            if (submitButton != null) submitButton.click();
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        if (event.getCharCode() == KeyCodes.KEY_ENTER)
        {
            if (submitButton != null) submitButton.click();
        }
    }

    public Button getSubmitButton()
    {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton)
    {
        this.submitButton = submitButton;
    }

}

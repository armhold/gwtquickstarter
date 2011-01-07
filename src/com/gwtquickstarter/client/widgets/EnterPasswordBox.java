package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * A simple TextBox extension that allows user to "submit" by pressing enter.
 * This allows us to simulate behavior that users expect- hitting enter submits the "form".
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class EnterPasswordBox extends PasswordTextBox implements KeyPressHandler
{
    private Button submitButton;

    public EnterPasswordBox()
    {
        addKeyPressHandler(this);
        setFocus(true);
    }

    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        if (event.getCharCode() == KeyCodes.KEY_ENTER)
        {
            if (submitButton != null) submitButton.click();
        }
    }

    public void setSubmitButton(Button submitButton)
    {
        this.submitButton = submitButton;
    }

}

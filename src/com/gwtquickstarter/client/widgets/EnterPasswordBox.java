package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * A simple TextBox extension that allows user to "submit" by pressing enter.
 * This allows us to simulate behavior that users expect- hitting enter submits the "form".
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class EnterPasswordBox extends PasswordTextBox
{
    public EnterPasswordBox()
    {
        setFocus(true);
    }

    public void setSubmitButton(Button submitButton)
    {
        addKeyPressHandler(new EnterTextBox.Handler(submitButton));
    }

}

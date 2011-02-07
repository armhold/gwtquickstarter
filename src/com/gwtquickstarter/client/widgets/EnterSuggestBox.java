package com.gwtquickstarter.client.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * A simple TextBox extension that allows user to "submit" by pressing enter.
 * This allows us to simulate behavior that users expect- hitting enter submits the "form".
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class EnterSuggestBox extends SuggestBox
{
    public EnterSuggestBox()
    {
    }

    public EnterSuggestBox(SuggestOracle oracle)
    {
        super(oracle);
    }

    public void setSubmitButton(Button submitButton)
    {
        addKeyPressHandler(new EnterTextBox.Handler(submitButton));
    }

}

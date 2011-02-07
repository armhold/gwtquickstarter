package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * A simple TextBox extension that allows user to "submit" by pressing enter.
 * This allows us to simulate behavior that users expect- hitting enter submits the "form".
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class EnterTextBox extends TextBox
{

    public EnterTextBox()
    {
        setFocus(true);
    }

    public void setSubmitButton(Button submitButton)
    {
        addKeyPressHandler(new Handler(submitButton));
    }

    public static class Handler implements KeyPressHandler
    {
        private Button submitButton;

        public Handler(Button submitButton)
        {
            this.submitButton = submitButton;
        }

        @Override
        public void onKeyPress(KeyPressEvent event)
        {
            // this "enter detection" is necessarily ugly. see: http://code.google.com/p/google-web-toolkit/issues/detail?id=5558#c6
            boolean isEnter;
            int charCode = event.getUnicodeCharCode();
            if (charCode == 0)
            {
                // it's probably Firefox
                int keyCode = event.getNativeEvent().getKeyCode();
                // beware! keyCode=40 means "down arrow", while charCode=40 means '('
                // always check the keyCode against a list of "known to be buggy" codes!
                isEnter = keyCode == KeyCodes.KEY_ENTER ;
            }
            else
            {
                isEnter = charCode == 13;
            }

            if (isEnter && submitButton != null)
            {
                submitButton.click();
            }
        }
    }

}

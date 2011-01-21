package com.gwtquickstarter.client.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class CenteredPopup extends PopupPanel
{
    @Override
    protected void onAttach()
    {
        super.onAttach();
        center();
    }
}

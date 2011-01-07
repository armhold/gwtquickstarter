package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class PushButtonWithToolTip extends PushButton implements MouseOverHandler
{
    protected String toolTipText;

    public PushButtonWithToolTip()
    {
        addHandler(this, MouseOverEvent.getType());
    }

    public PushButtonWithToolTip(Image upImage) {
        super(upImage);
        addHandler(this, MouseOverEvent.getType());
    }

    public PushButtonWithToolTip(Image upImage, Image downImage) {
        super(upImage, downImage);
        addHandler(this, MouseOverEvent.getType());
    }

    public PushButtonWithToolTip(Image upImage, Image downImage, ClickHandler handler) {
        super(upImage, downImage, handler);
        addHandler(this, MouseOverEvent.getType());
    }

    public void setToolTipText(String toolTipText)
    {
        this.toolTipText = toolTipText;
    }

    public String getToolTipText()
    {
        return toolTipText;
    }

    @Override
    public void onMouseOver(MouseOverEvent event)
    {
        ToolTip tip = new ToolTip(toolTipText, 0, 0);
    }
}

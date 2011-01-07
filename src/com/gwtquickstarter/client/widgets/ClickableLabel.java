package com.gwtquickstarter.client.widgets;

import com.google.gwt.user.client.ui.InlineLabel;

/**
 * essentially a link without history navigation
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ClickableLabel extends InlineLabel
{
    private ColorAnimation animation;

    public ClickableLabel()
    {
        addStyleName("clickableLabel");
    }

    public ClickableLabel(String text)
    {
        super(text);
        addStyleName("clickableLabel");
    }


    public void animate(String hexForegroundColor, String hexBackgroundColor)
    {
        if (animation == null)
        {
            animation = new ColorAnimation(hexForegroundColor, hexBackgroundColor, getElement(), "color", 750, true);
        }

        addStyleName("clickableLabelAnimating");
        animation.go();
    }

    public void cancelAnimation()
    {
        if (animation != null) animation.cancelLoop();

        removeStyleName("clickableLabelAnimating");
    }



}

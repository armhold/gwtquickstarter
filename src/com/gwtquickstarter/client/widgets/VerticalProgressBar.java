package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class VerticalProgressBar extends Composite {

    interface MyUiBinder extends UiBinder<HTMLPanel, VerticalProgressBar>
    {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    AbsolutePanel absolutePanel;

    @UiField
    AbsolutePanel slider;

    public VerticalProgressBar()
    {
        initWidget(uiBinder.createAndBindUi(this));
        setSize("30", "100%");

        absolutePanel.setSize("100%", "100%");
        absolutePanel.setWidgetPosition(slider, -1, 40);

    }

    public void setProgress(float progress)
    {
        int range = absolutePanel.getOffsetHeight() - slider.getOffsetHeight();
        absolutePanel.setWidgetPosition(slider, -1, (int) (progress * range));
    }

}

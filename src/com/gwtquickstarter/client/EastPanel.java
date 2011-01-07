package com.gwtquickstarter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtquickstarter.client.widgets.ClickableLabel;
import com.gwtquickstarter.client.widgets.NewFeedbackWidget;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class EastPanel extends Composite {
    interface MyUiBinder extends UiBinder<HTMLPanel, EastPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    ClickableLabel feedbackLink;

    @UiField
    VerticalPanel vpanel;


    public EastPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        feedbackLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                NewFeedbackWidget popup = new NewFeedbackWidget();
                popup.show();
            }
        });
    }

}

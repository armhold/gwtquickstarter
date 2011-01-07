package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class PopupLabel extends HTML implements ClickHandler
{
    protected String contentHTML;
    protected HTML popupContent = new HTML("&nbsp;");
//    protected DecoratedPopupPanel popup = new DecoratedPopupPanel();

    public PopupLabel()
    {
        addStyleName("popupLabel");
        popupContent.addStyleName("content");
        addClickHandler(this);
//        popup.setWidget(popupContent);
//
//        popup.addHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event)
//            {
//                popup.hide();
//            }
//        }, ClickEvent.getType());
    }

    public void setContentHTML(String contentHTML)
    {
        this.contentHTML = contentHTML;
        popupContent.setHTML(contentHTML);
    }

    @Override
    public void onClick(final ClickEvent event)
    {
        OkCancelDialog popup = new OkCancelDialog(contentHTML) {
            @Override
            public void onOkClicked()
            {
                hide();
            }

            @Override
            public void onCancelClicked()
            {
                hide();
            }

            @Override
            protected boolean wantCancelButton()
            {
                return false;
            }

            @Override
            protected void onAttach()
            {
                super.onAttach();
                setPopupPosition(event.getClientX(), event.getClientY());
            }
        };
        popup.setWidth("200px");

        popup.show();
    }

}

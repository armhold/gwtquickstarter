package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class OkCancelDialog extends CenteredPopup {

    protected ContentWidget contentWidget;
    protected boolean okToSubmit = true;

    public OkCancelDialog(String msg) {
        addStyleName("okCancelDialog");
        setAnimationEnabled(true);
        contentWidget = new ContentWidget(msg);
        setWidget(contentWidget);
    }

    protected String getOkText()
    {
        return "OK";
    }

    protected String getCancelText()
    {
        return "Cancel";
    }

    protected boolean wantCancelButton()
    {
        return true;
    }

    public abstract void onOkClicked();
    public abstract void onCancelClicked();

    class ContentWidget extends VerticalPanel
    {

        public ContentWidget(String msg) {
            Label label = new Label();
            label.setText(msg);

            Button okButton = new Button(getOkText());

            HorizontalPanel h = new HorizontalPanel();
            h.setSpacing(10);

            okButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    onOkClicked();
                    if (okToSubmit) {
                        hide();
                    }
                }
            });

            if (wantCancelButton())
            {
                Button cancelButton = new Button(getCancelText());
                cancelButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        onCancelClicked();
                        hide();
                    }
                });

                h.add(cancelButton);
            }

            add(label);
            h.add(okButton);
            add(h);
        }
    }
}

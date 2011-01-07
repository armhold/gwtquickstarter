package com.gwtquickstarter.client.widgets;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ToolTip extends PopupPanel {

    final int VISIBLE_DELAY = 2000;

    protected Timer removeDelay;

    public ToolTip(String message, int x, int y) {
        super(true);
        this.setPopupPosition(x, y);
        this.add(new Label(message));

        removeDelay = new Timer()
        {
            public void run()
            {
                ToolTip.this.setVisible(false);
                ToolTip.this.hide();
            }
        };

        removeDelay.schedule(VISIBLE_DELAY);
        addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent closeEvent) {
                removeDelay.cancel();
            }
        });

        addStyleName("toolTip");
        show();
    }

    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        switch (event.getTypeInt()) {
            case Event.ONMOUSEDOWN:
            case Event.ONCLICK: {
                hide();
            }
        }
    }
}

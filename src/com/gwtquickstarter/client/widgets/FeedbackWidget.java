package com.gwtquickstarter.client.widgets;

import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.util.AsyncCall;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class FeedbackWidget extends OkCancelDialog
{
    public static final StoreServiceAsync service = GWT.create(StoreService.class);
    protected final TextArea textArea = new TextArea();


    public FeedbackWidget()
    {
        super("We welcome your feedback");
        addStyleName("feedBackWidget");
        addStyleName("blueVerticalGradient");
        contentWidget.insert(textArea, 1);
    }

    @Override
    public void show() {
        super.show();
        textArea.setFocus(true);
    }

    @Override
    protected String getOkText() {
        return "Submit Feedback";
    }

    public void onOkClicked() {

        new AsyncCall<Void>("Submitting Feedback...") {
            protected void callService(AsyncCallback<Void> cb) {
                service.sendFeedback(textArea.getText(), null, cb);
            }

            public void onSuccess(Void v) {
                textArea.setText("");
                Window.alert("Thank you for your feedback.");
            }
        }.go();
    }

    public void onCancelClicked() {
    }

}

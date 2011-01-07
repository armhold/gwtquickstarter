package com.gwtquickstarter.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.ClientFactory;
import com.gwtquickstarter.client.util.AsyncCall;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class NewFeedbackWidget extends Composite {

    interface MyUiBinder extends UiBinder<HTMLPanel, NewFeedbackWidget> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    protected DialogBox dialogBox = new DialogBox();

    @UiField
    TextArea textArea;

    @UiField
    Button cancelButton;

    @UiField
    Button submitButton;

    @UiField
    Button thanksButton;

    public NewFeedbackWidget()
    {
        initWidget(uiBinder.createAndBindUi(this));
        dialogBox.setGlassEnabled(true);

//  animation seems to cause dialog box to be truncated
//        dialogBox.setAnimationEnabled(true);

        dialogBox.setTitle("We appreciate your feedback.");
        dialogBox.setText("We appreciate your feedback.");
        dialogBox.setWidget(this);
    }

    public void show()
    {
        dialogBox.center();
        dialogBox.show();
        textArea.setFocus(true);
    }

    public void hide()
    {
        dialogBox.hide();
    }

    @UiHandler("submitButton")
    public void submit(ClickEvent event)
    {
        new AsyncCall<Void>("Submitting Feedback...") {
            protected void callService(AsyncCallback<Void> cb) {
                ClientFactory.INSTANCE.getStoreService().sendFeedback(textArea.getText(), null, cb);
            }

            public void onSuccess(Void v) {
                textArea.setText("");
                textArea.setVisible(false);
                cancelButton.setVisible(false);
                submitButton.setVisible(false);

                thanksButton.setVisible(true);
                dialogBox.setTitle("Your feedback has been sent.");
            }
        }.go();

    }

    @UiHandler("cancelButton")
    public void cancel(ClickEvent event)
    {
        textArea.setText("");
        hide();
    }

    @UiHandler("thanksButton")
    public void thanksButtonClicked(ClickEvent event)
    {
        textArea.setText("");
        hide();
    }


}

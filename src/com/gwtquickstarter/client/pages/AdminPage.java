package com.gwtquickstarter.client.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwtquickstarter.client.BasePlace;
import com.gwtquickstarter.client.ClientFactory;
import com.gwtquickstarter.client.util.AsyncCall;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class AdminPage extends Page
{
    interface MyUiBinder extends UiBinder<HTMLPanel, AdminPage> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    Button submitButton;

    @UiField
    TextBox assetKeyField;

    @UiField
    TextBox maxUseCountField;

    @UiField
    HTML assetResultMessage;

    @UiField
    Button crawlButton;

    @UiField
    HTML crawlResultMessage;

    public AdminPage()
    {
        super("Admin");
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public boolean requiresAdmin()
    {
        return true;
    }

    public static class AdminPlace extends BasePlace
    {
    }

    @Prefix("!admin")
    public static class Tokenizer implements PlaceTokenizer<AdminPage.AdminPlace>
    {
        @Override
        public String getToken(AdminPage.AdminPlace place)
        {
            return "";
        }

        @Override
        public AdminPlace getPlace(String token)
        {
            return new AdminPage.AdminPlace();
        }
    }

    @UiHandler("assetKeyField")
    public void assetFieldClicked(ClickEvent event)
    {
        assetKeyField.setText("");
    }

    @UiHandler("submitButton")
    public void submitClicked(ClickEvent event)
    {
        new AsyncCall() {
            @Override
            protected void callService(AsyncCallback cb) {
                String key = assetKeyField.getText();
                int maxUses = Integer.parseInt(maxUseCountField.getText());

                ClientFactory.INSTANCE.getStoreService().createActivationAsset(key, maxUses, cb);
            }

            @Override
            public void onSuccess(Object result) {
                assetResultMessage.setText("asset created");
                assetResultMessage.removeStyleName("error");
                assetResultMessage.addStyleName("success");
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                assetResultMessage.setText(t.getMessage());
                assetResultMessage.addStyleName("error");
                assetResultMessage.removeStyleName("success");
            }
        }.go();
    }

    @UiHandler("crawlButton")
    public void crawlButtonClicked(ClickEvent event)
    {
        new AsyncCall<Void>()
        {
            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                ClientFactory.INSTANCE.getStoreService().updateAjaxCrawlerCache(cb);
            }

            @Override
            public void onSuccess(Void result)
            {
                crawlResultMessage.setText("crawl request submitted");
                crawlResultMessage.removeStyleName("error");
                crawlResultMessage.addStyleName("success");
            }

            @Override
            public void onFailure(Throwable t)
            {
                crawlResultMessage.setText("error: " + t.getMessage());
                crawlResultMessage.removeStyleName("success");
                crawlResultMessage.addStyleName("error");
            }
        }.go();
    }

}

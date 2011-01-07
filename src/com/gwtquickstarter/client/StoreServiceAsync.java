package com.gwtquickstarter.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtquickstarter.client.model.*;

/**
 * The async counterpart of <code>StoreService</code>.
 *
 * @author Copyright (c) 2011 George Armhold
 */
public interface StoreServiceAsync
{
    public void sendFeedback(String feedback, String fromEmail, AsyncCallback<Void> async);
    public void addToMailingList(String email, AsyncCallback<Void> async);
    public void buyNow(String merchantId, String productId, int quantity, boolean sandbox, AsyncCallback<String> async);
    public void createAccount(String assetKey, String username, String password, AsyncCallback<Void> async);
    public void login(String username, String password, boolean rememberMe, AsyncCallback<LoginResult> async);
    public void getAppConfiguration(AsyncCallback<AppConfiguration> async);
    public void loginViaCookie(String cookieContents, AsyncCallback<LoginResult> async);
    public void logout(AsyncCallback<Void> async);
    public void changePassword(String currentPassword, String newPassword, AsyncCallback<Void> async);
    public void resetPassword(String username, AsyncCallback<Void> async);
    public void changeEmail(String newEmailAddress, AsyncCallback<Void> async);
    public void createActivationAsset(String key, int maxUses, AsyncCallback<Void> async);
    public void updateAjaxCrawlerCache(AsyncCallback<Void> async);

}

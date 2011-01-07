package com.gwtquickstarter.client;

import com.google.gwt.user.client.rpc.*;
import com.gwtquickstarter.client.model.*;
import com.gwtquickstarter.client.util.*;

/**
 * The client side stub for the RPC service.
 *
 * @author Copyright (c) 2011 George Armhold
 */
@RemoteServiceRelativePath("store")
public interface StoreService extends RemoteService
{
    public void sendFeedback(String feedback, String fromEmail);
    public void addToMailingList(String email);
    public String buyNow(String merchantId, String productId, int quantity, boolean sandbox);
    public void createAccount(String accountCode, String username, String password) throws InvalidAssetException, InvalidLoginException;
    public void changePassword(String currentPassword, String newPassword) throws InvalidLoginException;
    public void resetPassword(String username) throws InvalidLoginException;
    public void changeEmail(String newEmailAddress) throws InvalidLoginException;
    public LoginResult loginViaCookie(String cookieContents) throws InvalidLoginException;
    public LoginResult login(String username, String password, boolean rememberMe) throws InvalidLoginException;
    public void logout();
    public AppConfiguration getAppConfiguration();
    public void createActivationAsset(String key, int maxUses) throws NotLoggedInException;
    public void updateAjaxCrawlerCache();
}

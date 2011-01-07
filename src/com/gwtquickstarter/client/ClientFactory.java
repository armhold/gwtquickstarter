package com.gwtquickstarter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.*;
import com.google.gwt.place.shared.*;
import com.gwtquickstarter.client.model.*;
import com.gwtquickstarter.client.util.LoginSupport;

/**
 * see http://code.google.com/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html#ClientFactory
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ClientFactory
{
    public static final ClientFactory INSTANCE = new ClientFactory();


    private ClientFactory() { }

    protected StoreServiceAsync service = GWT.create(StoreService.class);
    protected EventBus eventBus = new SimpleEventBus();
    protected PlaceController placeController = new PlaceController(eventBus);
    protected AppConfiguration appConfig;
    protected LoginResult loggedInUser;
    protected Place postLoginPlace;
    protected LoginSupport loginSupport = new LoginSupport();

    public StoreServiceAsync getStoreService()
    {
        return service;
    }

    public LoginSupport getLoginSupport()
    {
        return loginSupport;
    }

    public Place getPostLoginPlace()
    {
        return postLoginPlace;
    }

    public void setPostLoginPlace(Place postLoginPlace)
    {
        this.postLoginPlace = postLoginPlace;
    }

    public boolean isUserLoggedIn()
    {
        return getLoggedInUser() != null;
    }

    public boolean isAdmin()
    {
        return getLoggedInUser() != null && getLoggedInUser().isAdmin();
    }

    public LoginResult getLoggedInUser()
    {
        return loggedInUser;
    }

    public void setLoggedInUser(LoginResult loggedInUser)
    {
        this.loggedInUser = loggedInUser;
    }

    public EventBus getEventBus()
    {
        return eventBus;
    }

    public PlaceController getPlaceController()
    {
        return placeController;
    }

    public AppConfiguration getAppConfig()
    {
        return appConfig;
    }

    public void setAppConfig(AppConfiguration appConfig)
    {
        this.appConfig = appConfig;
    }

    public StoreServiceAsync getService()
    {
        return service;
    }

    public void setService(StoreServiceAsync service)
    {
        this.service = service;
    }

}

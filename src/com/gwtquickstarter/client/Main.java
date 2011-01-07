package com.gwtquickstarter.client;

import com.google.gwt.activity.shared.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.gwtquickstarter.client.pages.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class Main implements EntryPoint, ActivityMapper
{
    public void onModuleLoad()
    {
        SimplePanel pageActivityPanel = new SimplePanel();

//        ScrollPanel sp = new ScrollPanel(new ContentPage(pageActivityPanel));
//        pageActivityPanel.setHeight("100%");

//        RootPanel.get("middle").add(sp);
        RootPanel.get("middle").add(pageActivityPanel);

        EventBus eventBus = ClientFactory.INSTANCE.getEventBus();
        PlaceController placeController = ClientFactory.INSTANCE.getPlaceController();

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityManager activityManager = new ActivityManager(this, eventBus);
        activityManager.setDisplay(pageActivityPanel);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        PlaceHistoryMapper historyMapper = getHistoryMapper();
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, getDefaultPlace());

        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();

        // update anchors for the current ajax link
        // you must have a div with matching id "container"
        new CurrentAnchorUpdater(DOM.getElementById("container"));
    }

    /**
     * @return the default Place the user should be sent to if none specified in the URL
     */
    protected Place getDefaultPlace()
    {
        return null;
    }

    /**
     * implement ActivityMapper- your Main subclass should override this, and fall back to super.getActivity()
     */
    @Override
    public Activity getActivity(Place place)
    {
        if (place instanceof DemoPage.DemoPlace)
        {
            DemoPage page = new DemoPage();
            return new DemoPage.DemoActivity(page, (DemoPage.DemoPlace) place);
        }
        else if (place instanceof LoginPage.LoginPlace)
        {
            LoginPage page = new LoginPage();
            return page.createActivity((LoginPage.LoginPlace) place);
        }
        else if (place instanceof CreateAccountPage.CreateAccountPlace)
        {
            CreateAccountPage page = new CreateAccountPage();
            return page.createActivity(place);
        }
        else if (place instanceof ChangePasswordPage.ChangePasswordPlace)
        {
            ChangePasswordPage page = new ChangePasswordPage();
            return new ChangePasswordPage.ChangePasswordActivity(page, (ChangePasswordPage.ChangePasswordPlace) place);
        }
        else if (place instanceof ChangeEmailPage.ChangeEmailPlace)
        {
            ChangeEmailPage page = new ChangeEmailPage();
            return new ChangeEmailPage.ChangeEmailActivity(page, (ChangeEmailPage.ChangeEmailPlace) place);
        }

        return null;
    }

    /**
     * You must implement this to return an implementation of PlaceHistoryMapper.
     * Your implementation should be annotated with "@WithTokenizers", enumerating the
     * Tokenizers for each of your pages.
     */
    public abstract PlaceHistoryMapper getHistoryMapper();
}

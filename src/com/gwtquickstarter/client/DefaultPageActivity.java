package com.gwtquickstarter.client;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.gwtquickstarter.client.pages.*;
import com.gwtquickstarter.client.util.ClientUtils;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class DefaultPageActivity extends AbstractActivity
{
    protected Page page;
    protected Place place;

    public DefaultPageActivity(Page page, Place place)
    {
        this.page = page;
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus)
    {
        if (place instanceof BasePlace)
        {
            String googleAnalyticsId = ClientConfiguration.DEFAULT_INSTANCE.getString("ga_id");
            if (googleAnalyticsId != null)
            {
                ClientUtils.trackGoogleAnalytics(googleAnalyticsId, ((BasePlace) place).getGoogleAnalyticsName());
            }
        }


        if (page.requiresHttps())
        {
// disabled until we can make ssl work on App Engine for custom domains
//
//            redirectToHttpsIfNeeded();
        }

        if (page.requiresLogin() && ! ClientFactory.INSTANCE.isUserLoggedIn())
        {
            ClientFactory.INSTANCE.setPostLoginPlace(place);
            ClientFactory.INSTANCE.getPlaceController().goTo(new LoginPage.LoginPlace());
            return;
        }

//        if (page.requiresAdmin() && ! ClientFactory.INSTANCE.isAdmin())
//        {
//            ClientFactory.INSTANCE.setPostLoginPlace(place);
//            ClientFactory.INSTANCE.getPlaceController().goTo(new LoginPage.LoginPlace());
//            return;
//        }

        if (! ClientUtils.isEmpty(page.getTitle()))
        {
            Window.setTitle(page.getTitle());
        }

        if (! ClientUtils.isEmpty(page.getMetaDescription()))
        {
            updateMeta("metaDescription", page.getMetaDescription());
        }

        if (! ClientUtils.isEmpty(page.getMetaKeywords()))
        {
            updateMeta("metaKeywords", page.getMetaKeywords());
        }

        if (! ClientUtils.isEmpty(page.getByLine()))
        {
            updateByline(page.getByLine());
        }
        containerWidget.setWidget(page);
    }

    protected void updateMeta(String metaId, String content)
    {
        Element meta = DOM.getElementById(metaId);
        meta.setAttribute("content", content);
    }

    protected void updateByline(String innerTextContent)
    {
        Element bylineElement = DOM.getElementById("byline");
        if (bylineElement != null)
        {
            bylineElement.setInnerText(innerTextContent);
        }
    }

    private void redirectToHttpsIfNeeded()
    {
        // https doesn't work in DevMode
        if (! GWT.isScript())
        {
            return;
        }

        String proto = Window.Location.getProtocol();
        if (! proto.equals("https:"))
        {
            String loc = Window.Location.getHref();
            String newLoc = loc.replaceFirst(proto, "https:");
            Window.Location.replace(newLoc);
        }
    }

}

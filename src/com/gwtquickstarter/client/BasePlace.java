package com.gwtquickstarter.client;

import com.google.gwt.place.shared.Place;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class BasePlace extends Place
{

    public String getGoogleAnalyticsName()
    {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}

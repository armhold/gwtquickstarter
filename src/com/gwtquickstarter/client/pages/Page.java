package com.gwtquickstarter.client.pages;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Composite;
import com.gwtquickstarter.client.DefaultPageActivity;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class Page extends Composite
{
    protected final String title;

    public Page(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public boolean requiresHttps()
    {
        return false;
    }

    public boolean requiresLogin()
    {
        return false;
    }

    public boolean requiresAdmin()
    {
        return false;
    }

    public boolean isActive()
    {
        return getParent() != null;
    }

    public Activity createActivity(Place place)
    {
        return new DefaultPageActivity(this, place);
    }

    public String getMetaDescription()
    {
        return null;
    }

    public String getMetaKeywords()
    {
        return null;
    }

    public String getByLine()
    {
        return null;
    }

}

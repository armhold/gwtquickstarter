package com.gwtquickstarter.client.events;

import com.google.gwt.event.shared.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class PlaceChangeEvent extends GwtEvent<PlaceChangeEvent.Handler>
{
    public static Type<PlaceChangeEvent.Handler> TYPE = new Type<PlaceChangeEvent.Handler>();

    private final String place;

    public PlaceChangeEvent(String place)
    {
        this.place = place;
    }

    @Override
    public Type<PlaceChangeEvent.Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.onPlaceChange(place);
    }

    public static interface Handler extends EventHandler
    {
        public void onPlaceChange(String newPlace);
    }

}

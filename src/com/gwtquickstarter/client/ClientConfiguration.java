package com.gwtquickstarter.client;

import java.util.MissingResourceException;
import com.google.gwt.i18n.client.Dictionary;

/**
 * handy wrapper for client-side configuration; avoids MissingResourceException if not present in host page
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ClientConfiguration
{
    public static final ClientConfiguration DEFAULT_INSTANCE = new ClientConfiguration("ClientConfiguration");

    protected Dictionary dict;

    public ClientConfiguration(String configName)
    {
        try
        {
            dict = Dictionary.getDictionary(configName);
        } catch (MissingResourceException e)
        {
            dict = null;
        }
    }

    public String getString(String key)
    {
        try
        {
            return dict == null ? null : dict.get(key);
        } catch (MissingResourceException e)
        {
            return null;
        }
    }

}

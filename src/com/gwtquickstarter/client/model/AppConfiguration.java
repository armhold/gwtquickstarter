package com.gwtquickstarter.client.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class AppConfiguration implements Serializable
{
    private static final long serialVersionUID = 4187922259632335215L;

    private Map<String, String> properties = new HashMap<String, String>();

    public AppConfiguration()
    {
    }

    public void setProperty(String property, String value)
    {
        properties.put(property, value);
    }

    public String getStringProperty(String property)
    {
        return properties.get(property);
    }

    public Boolean getBooleanProperty(String property)
    {
        return Boolean.valueOf(properties.get(property));
    }

    public int getIntProperty(String property)
    {
        return Integer.valueOf(properties.get(property));
    }

}

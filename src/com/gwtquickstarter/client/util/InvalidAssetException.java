package com.gwtquickstarter.client.util;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class InvalidAssetException extends Exception implements Serializable
{
    private static final long serialVersionUID = -3060698509168632858L;

    private String assetRef;

    public InvalidAssetException()
    {
    }

    public InvalidAssetException(String message, String assetRef)
    {
        super(message);
        this.assetRef = assetRef;
    }

    public String getAssetRef()
    {
        return assetRef;
    }

}

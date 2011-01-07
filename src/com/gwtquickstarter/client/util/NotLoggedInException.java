package com.gwtquickstarter.client.util;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class NotLoggedInException extends Exception implements Serializable
{
    private static final long serialVersionUID = 2326679223250793933L;

    public NotLoggedInException()
    {
        this("login required");
    }

    public NotLoggedInException(String message)
    {
        super(message);
    }

}

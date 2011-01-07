package com.gwtquickstarter.client.util;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class InvalidLoginException extends Exception implements Serializable
{
    private static final long serialVersionUID = 2326679223250793933L;

    public InvalidLoginException()
    {
    }

    public InvalidLoginException(String message)
    {
        super(message);
    }

}

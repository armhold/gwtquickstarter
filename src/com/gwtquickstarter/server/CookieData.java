package com.gwtquickstarter.server;

import java.io.Serializable;
import javax.persistence.Id;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class CookieData implements Serializable
{
    private static final long serialVersionUID = 5030444946526583656L;

    @Id
    private String username;
    private String token;

    public CookieData()
    {
    }

    public CookieData(String username, String token)
    {
        this.token = token;
        this.username = username;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}

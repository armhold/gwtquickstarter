package com.gwtquickstarter.client.model;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class LoginResult implements Serializable
{
    private static final long serialVersionUID = -6002885028445250056L;

    public static final String COOKIE_ID = "rememberMe";
    
    private String username;
    private String cookieContents;
    private boolean admin;

    public LoginResult()
    {
    }

    public LoginResult(String username)
    {
        this.username = username;
    }

    public LoginResult(String username, String cookieContents)
    {
        this.username = username;
        this.cookieContents = cookieContents;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCookieContents()
    {
        return cookieContents;
    }

    public void setCookieContents(String cookieContents)
    {
        this.cookieContents = cookieContents;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}

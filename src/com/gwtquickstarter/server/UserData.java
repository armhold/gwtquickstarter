package com.gwtquickstarter.server;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class UserData implements Serializable
{
    private static final long serialVersionUID = -4858012037325011985L;

    @Id
    private String username;

    private String email;
    private String hashedPassword;
    private String salt;
    private Date creationDate;
    private Date lastLogin;
    private boolean admin;

    public UserData()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

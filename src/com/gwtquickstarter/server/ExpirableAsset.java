package com.gwtquickstarter.server;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class ExpirableAsset implements Serializable
{
    private static final long serialVersionUID = -7765449577834189532L;

    @Id
    protected String key;

    protected int maxUseCount = 3;
    protected int numberOfUses;
    protected Date creationDate;
    protected Date lastUseDate;
    protected boolean notifyUponAccess;

    public ExpirableAsset()
    {
        creationDate = new Date();
    }

    public boolean hasUsesRemaining()
    {
        return getNumberOfUses() < getMaxUseCount();
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public int getNumberOfUses()
    {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses)
    {
        this.numberOfUses = numberOfUses;
    }

    public int getMaxUseCount()
    {
        return maxUseCount;
    }

    public void setMaxUseCount(int maxUseCount)
    {
        this.maxUseCount = maxUseCount;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getLastUseDate()
    {
        return lastUseDate;
    }

    public void setLastUseDate(Date lastUseDate)
    {
        this.lastUseDate = lastUseDate;
    }

    public boolean isNotifyUponAccess() {
        return notifyUponAccess;
    }

    public void setNotifyUponAccess(boolean notifyUponAccess) {
        this.notifyUponAccess = notifyUponAccess;
    }

}

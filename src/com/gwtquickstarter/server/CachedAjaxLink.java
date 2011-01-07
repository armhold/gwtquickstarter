package com.gwtquickstarter.server;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class CachedAjaxLink implements Serializable
{
    private static final long serialVersionUID = -3797091523981960228L;

    @Id
    private String href;
    private String cachedContent;
    private Date dateCached;


    public CachedAjaxLink()
    {
    }

    public String getCachedContent()
    {
        return cachedContent;
    }

    public void setCachedContent(String cachedContent)
    {
        this.cachedContent = cachedContent;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    public Date getDateCached()
    {
        return dateCached;
    }

    public void setDateCached(Date dateCached)
    {
        this.dateCached = dateCached;
    }

}

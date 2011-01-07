package com.gwtquickstarter.server;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class DownloadableAsset extends ExpirableAsset implements Serializable
{
    private static final long serialVersionUID = -7765449577834189532L;

    private String filePath;
    private String contentType;

    public DownloadableAsset()
    {
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

}

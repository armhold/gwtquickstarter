package com.gwtquickstarter.server;

import java.io.*;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class DownloadServlet extends HttpServlet
{
    private static final long serialVersionUID = -4571855646952165388L;
    protected static final Logger log = Logger.getLogger(DownloadServlet.class.getName());
    private static final DAO dao = new DAO();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/"))
        {
            throw new ServletException("expirable asset invalid");
        }

        // strip off leading "/"
        String assetKey = pathInfo.substring(1, pathInfo.length());

        DownloadableAsset asset = dao.getDownloadableAssetByKey(assetKey);
        if (asset == null)
        {
            throw new ServletException("expirable asset invalid");
        }

        validateAsset(asset);
        sendDownloadableAsset(res, asset);
    }

    protected void validateAsset(ExpirableAsset asset) throws ServletException
    {
        if (asset.getNumberOfUses() >= asset.getMaxUseCount())
        {
            throw new ServletException("expirable URL has already been downloaded");
        }
    }


    protected void sendDownloadableAsset(HttpServletResponse res, DownloadableAsset asset) throws ServletException, IOException
    {
        File assetDir = new File(getServletContext().getRealPath("assets"));
        File dataFile = new File(assetDir, asset.getFilePath());

        log.info("file path: " + dataFile.getAbsolutePath());
        log.info("file path: " + dataFile.getAbsolutePath());

        if (! dataFile.exists())
        {
            throw new ServletException("invalid asset");
        }

        res.setHeader("Content-disposition","attachment; filename=" + dataFile.getName());

        if (dataFile.getName().endsWith(".jpg"))
        {
            res.setContentType("image/jpg");
        }

        ServerUtils.copyStream(new FileInputStream(dataFile), res.getOutputStream());
        dao.incrementUses(asset);
    }

}

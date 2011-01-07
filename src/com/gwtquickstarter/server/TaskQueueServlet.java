package com.gwtquickstarter.server;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * A servlet to handle Google App Engine Task Queue requests.
 * Upon request, we crawl the given Ajax link, then cache the link content in the data store.
 *
 * You'll need to configure this servlet in your web.xml like this:
 *
 *   <servlet>
 *     <servlet-name>taskQueueServlet</servlet-name>
 *     <servlet-class>com.gwtquickstarter.server.TaskQueueServlet</servlet-class>
 *   </servlet>
 *
 *   <servlet-mapping>
 *     <servlet-name>taskQueueServlet</servlet-name>
 *     <url-pattern>/your_appname/taskQueue</url-pattern>
 *   </servlet-mapping>
 *
 *   <context-param>
 *     <param-name>taskQueuePath</param-name>
 *     <param-value>/your_appname/taskQueue</param-value>
 *   </context-param>
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class TaskQueueServlet extends HttpServlet
{
    private static final long serialVersionUID = 3726375173849653430L;
    protected static final Logger log = Logger.getLogger(TaskQueueServlet.class.getName());
    private static DAO dao = new DAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String encodedUrl = req.getParameter("encodedUrl");
        if (encodedUrl == null)
        {
            throw new IllegalArgumentException("missing param: encodedUrl");
        }

        String timeStamp = req.getParameter("timeStamp");
        if (timeStamp == null)
        {
            throw new IllegalArgumentException("missing param: timeStamp");
        }


        String decodedUrl = ServerUtils.decodeURL(encodedUrl);
        URL urlToCrawl = new URL(decodedUrl);

        // check to see if another task has already crawled and cached this url
        String href = urlToCrawl.getRef();
        CachedAjaxLink cachedAjaxLink = dao.getCachedAjaxLink(href);
        Date date = ServerUtils.toDate(timeStamp);
        if (cachedAjaxLink != null && cachedAjaxLink.getDateCached().getTime() >= date.getTime())
        {
            log.severe("already cached: " + href + ", on date: " + timeStamp);
        }
        else
        {
            log.severe("not yet cached: " + href + ", queuing");
            getServletContext().getInitParameter("taskQueuePath");
            AjaxCacher cacher = new AjaxCacher(getServletContext().getInitParameter("taskQueuePath"));
            cacher.crawl(urlToCrawl, ServerUtils.toDate(timeStamp));
        }

    }

}

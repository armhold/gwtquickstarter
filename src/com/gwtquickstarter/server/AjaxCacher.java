package com.gwtquickstarter.server;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.*;

/**
 * Caches html content for a URL as CachedAjaxLink objects in the data store.
 * Also, for each ajax link found in the content, we queue a new App Engine task
 * for crawling the link.
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class AjaxCacher
{
    protected static final Logger log = Logger.getLogger(AjaxCacher.class.getName());
    protected static final DAO dao = new DAO();

    public static final long PUMP_TIME = 5000;
    protected WebClient webClient;
    protected String crawlServletUrl;

    public AjaxCacher(String crawlServletUrl)
    {
        this.crawlServletUrl = crawlServletUrl;
        webClient = Holder.get();
    }

    public void crawl(URL urlToCrawl, Date crawlRequestTimestamp)
    {
        // URLs we've already queued
        Set<URL> queuedURLs = new HashSet<URL>();
        queuedURLs.add(urlToCrawl);

        try
        {
            log.severe("crawling url: " + urlToCrawl.toString());
            HtmlPage page = webClient.getPage(urlToCrawl);

            // appengine hack because it's single threaded
            webClient.getJavaScriptEngine().pumpEventLoop(PUMP_TIME);

            String pageContent = page.asXml();

            CachedAjaxLink cachedAjaxLink = new CachedAjaxLink();
            cachedAjaxLink.setHref(urlToCrawl.getRef());
            cachedAjaxLink.setCachedContent(pageContent);
            cachedAjaxLink.setDateCached(new Date());  // time actually cached
            dao.updateCachedAjaxLink(cachedAjaxLink);

            List<HtmlAnchor> anchors = page.getAnchors();
            for (HtmlAnchor anchor : anchors)
            {
                // only care about ajax links
                if (! anchor.getHrefAttribute().startsWith("#")) continue;

                URL newUrl = new URL(urlToCrawl, anchor.getHrefAttribute());

                // don't queue multiple requests for the same URL
                if (queuedURLs.contains(newUrl)) continue;

                queuedURLs.add(newUrl);

                // prevent loops
                CachedAjaxLink link = dao.getCachedAjaxLink(newUrl.getRef());
                if (link == null || link.getDateCached().getTime() < crawlRequestTimestamp.getTime())
                {
                    queueCrawlRequest(newUrl.toString(), crawlRequestTimestamp);
                }
            }

        } catch (IOException e)
        {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            webClient.closeAllWindows();
        }
    }

    /**
     * submits a crawl request to the queue; TaskQueueServlet will then handle the request asynchronously
     */
    public void queueCrawlRequest(String urlToCrawl, Date timeStamp)
    {
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions options = TaskOptions.Builder.url(crawlServletUrl);
        options.param("encodedUrl", ServerUtils.encodeURL(urlToCrawl));
        options.param("timeStamp", ServerUtils.fromDate(timeStamp));
        options.method(TaskOptions.Method.GET);
        queue.add(options);
    }

    /**
     * try to cache a copy of the WebClient in ThreadLocal for faster startups on Google App Engine
     */
    public static class Holder
    {
        private static ThreadLocal<WebClient> holder = new ThreadLocal<WebClient>()
        {
            protected synchronized WebClient initialValue()
            {
                WebClient result = new WebClient(BrowserVersion.FIREFOX_3);
                result.setWebConnection(new UrlFetchWebConnection(result));
                return result;
            }
        };

        public static WebClient get()
        {
            return holder.get();
        }
    }

}

package com.gwtquickstarter.server;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.http.*;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A servlet filter that will attempt to serve requests for Ajax links with cached HTML snapshots.
 * @see http://code.google.com/web/ajaxcrawling/docs/getting-started.html
 *
 *
 * You will need to configure this filter in your web.xml like this:
 *
 *   <filter>
 *     <filter-name>AjaxCrawlFilter</filter-name>
 *     <filter-class>com.gwtquickstarter.server.AjaxCrawlFilter</filter-class>
 *   </filter>
 *
 *   <filter-mapping>
 *     <filter-name>AjaxCrawlFilter</filter-name>
 *     <url-pattern>/*</url-pattern>
 *   </filter-mapping>
 *
 * adapted from http://www.ozdroid.com/#!BLOG/2010/10/12/How_to_Make_Google_AppEngine_Applications_Ajax_Crawlable
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class AjaxCrawlFilter implements Filter
{
    public static final String SCHEME = "http";
    public static final long PUMP_TIME = 5000;
    protected WebClient webClient;
    protected DAO dao = new DAO();
    private static final Logger log = Logger.getLogger(AjaxCrawlFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        webClient = new WebClient(BrowserVersion.FIREFOX_3);
        webClient.setWebConnection(new UrlFetchWebConnection(webClient));
    }

    @Override
    public void destroy()
    {
        if (webClient != null)
        {
            webClient.closeAllWindows();
        }
    }

    /**
     * @return the href corresponding to the content shown on your landing page when no fragment is specified
     */
    protected String getLandingPageHref()
    {
        return "!home:";
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String queryString = req.getQueryString();

        /**
         * if we see _escaped_fragment_, Googlebot is querying us for an ajax link
         */
        if ((queryString != null) && (queryString.contains("_escaped_fragment_=")))
        {
            handleAjaxLinkRequest(req, response);
        }
        else
        {
            try
            {
                /*
                 * not an _escaped_fragment_ URL, so move up the chain of
                 * servlet (filters)
                 */
                chain.doFilter(request, response);

            } catch (ServletException e)
            {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    protected void handleAjaxLinkRequest(HttpServletRequest req, ServletResponse res) throws IOException
    {
        String uri = req.getRequestURI();
        int port = req.getServerPort();
        String domain = req.getServerName();

        // rewrite the URL back to the original #! version
        // remember to unescape any %XX characters
        String url_with_hash_fragment = uri + rewriteQueryString(req.getQueryString());

        log.severe("url_with_hash_fragment: " + url_with_hash_fragment);

        if (url_with_hash_fragment.endsWith("practice:Holidays:thanksgiving.xml") ||
                url_with_hash_fragment.endsWith("createAccount:StartUpLift")      ||
                url_with_hash_fragment.endsWith("account:"))
        {
            log.severe("manually sending 404 for: " + url_with_hash_fragment);
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        URL url = new URL(SCHEME, domain, port, url_with_hash_fragment);
        String href = url.getRef();

        // if there is no fragment specified, consider this as a request for the ajax version of the "home" page
        if (href == null || href.equals("") || href.equals("!"))
        {
            href = getLandingPageHref();
            log.severe("mapped empty href to: " + href);
        }
        CachedAjaxLink cachedAjaxLink = dao.getCachedAjaxLink(href);

        // first check to see if we have a cached version of the link content
        //
        String result;
        if (cachedAjaxLink != null)
        {
            log.severe("cachedAjaxLink found for: " + url.toString());
            result = cachedAjaxLink.getCachedContent();
        }
        else
        {
            // try using HtmlUnit to parse the link content in real-time.
            // most likely this will exceed the Google App Engine time limit
            // and fail.
            //
            log.severe("cachedAjaxLink NOT found for: " + url.toString());
            log.severe("cachedAjaxLink NOT found for href: " + href);
            HtmlPage page = webClient.getPage(url);

            // gae hack because it's single threaded
            webClient.getJavaScriptEngine().pumpEventLoop(PUMP_TIME);
            result = page.asXml();
        }

        ServletOutputStream out = res.getOutputStream();
        out.println(result);
        out.flush();
    }


    public String rewriteQueryString(String url_with_escaped_fragment)
    {
        try
        {
            String decoded = URLDecoder.decode(url_with_escaped_fragment, "UTF-8");

            // this helps running on development mode
            String gwt = decoded.replace("gwt", "?gwt");

            String unescapedAmp = gwt.replace("&_escaped_fragment_=", "#!");
            return unescapedAmp.replace("_escaped_fragment_=", "#!");
        } catch (UnsupportedEncodingException e)
        {
            log.log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
    }

}

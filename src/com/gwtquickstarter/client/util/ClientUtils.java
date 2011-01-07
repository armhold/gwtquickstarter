package com.gwtquickstarter.client.util;

import com.google.gwt.core.client.GWT;

/**
 * handy utility methods
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ClientUtils
{
    public static boolean emailIsValid(String email)
    {
        if (isEmpty(email)) return false;

        String[] parts = email.split("@");
        return parts.length == 2 && isNotEmpty(parts[0]) && isNotEmpty(parts[1]);
    }

    public static boolean isEmpty(String s)
    {
        return s == null || s.equals("");
    }

    public static boolean isNotEmpty(String s)
    {
        return ! isEmpty(s);
    }

    public static boolean same(String s1, String s2)
    {
        if (s1 == null)
        {
            return s2 == null;
        }
        else
        {
            return s1.equals(s2);
        }
    }

    public static native void playAudioTag(String audioId) /*-{
        $doc.getElementById(audioId).play();
    }-*/;

    public static native String getUserAgent() /*-{
        return navigator.userAgent.toLowerCase();
    }-*/;


    public static boolean isMSIE()
    {
        return getUserAgent().toLowerCase().contains("msie");
    }


    public static void trackGoogleAnalytics(String gaAccount, String historyToken)
    {
        if (GWT.isScript()) trackGoogleAnalyticsNative(gaAccount, historyToken);
    }

    /**
     * from: http://code.google.com/p/gwt-examples/source/browse/trunk/DemoGoogleAnalytics/src/org/gonevertical/demo/client/Track.java
     *
     * trigger google analytic native js - included in the build
     *   CHECK - *.gwt.xml for -> <script src="../ga.js"/>
     *
     *   http://code.google.com/intl/en-US/apis/analytics/docs/gaJS/gaJSApiEventTracking.html
     *
     * @param historyToken
     */
    public static native void trackGoogleAnalyticsNative(String gaAccount, String historyToken) /*-{

      try {

          // setup tracking object with account
          var pageTracker = $wnd._gat._getTracker(gaAccount);

          pageTracker._setRemoteServerMode();

          // turn on anchor observing
          pageTracker._setAllowAnchor(true);

          // send event to google server
          pageTracker._trackPageview(historyToken);

      } catch(err) {

        // debug
        alert('FAILURE: to send in event to google analytics: ' + err);
      }

   }-*/;

}

package com.gwtquickstarter.server;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ServerUtils
{
    protected static final Logger log = Logger.getLogger(ServerUtils.class.getName());
    protected static final String DATE_FORMAT = "yyyy.MM.dd-HH:mm:ss";

    public static void sendDefaultEmail(String subject, String message) {
        final String fromAddr = System.getProperty("emailFromAddress");
        final String toAddr = System.getProperty("emailFromAddress");

        final String fromName= fromAddr;
        final String toName= fromAddr;

        sendEmail(fromAddr, fromName, toAddr, toName, subject, message);
    }

    public static void sendEmail(String fromAddr, String fromName, String toAddr, String toName, String subject, String message) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message m = new MimeMessage(session);
            m.setFrom(new InternetAddress(fromAddr, fromName));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr, toName));
            m.setSubject(subject);
            m.setText(message);
            Transport.send(m);
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        log.info("successfully sent email to: " + toAddr);
    }

    public static long copyStream(InputStream in, OutputStream out) throws IOException
    {
        int BUFSIZE = 8192;
        byte[] buf = new byte[BUFSIZE];

        BufferedInputStream bin = new BufferedInputStream(in, BUFSIZE);
        BufferedOutputStream bout = new BufferedOutputStream(out, BUFSIZE);

        try
        {
            int c;
            long bytesWritten = 0l;

            while ((c = bin.read(buf)) != -1)
            {
                bout.write(buf, 0, c);
                bytesWritten += c;
            }

            return bytesWritten;
        } finally
        {
            bin.close();
            bout.close();
        }
    }

    public static String readAsString(InputStream is)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buf = new StringBuffer();

        try
        {
            String s;
            while ((s = in.readLine()) != null)
            {
                buf.append(s);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        } finally
        {
            try
            {
                in.close();
            } catch (IOException ignored)
            {
            }
        }

        return buf.toString();
    }

    public static String encodeURL(String url)
    {
        try
        {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decodeURL(String url)
    {
        try
        {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static String fromDate(Date date)
    {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        return fmt.format(date);
    }

    public static Date toDate(String date)
    {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);

        try
        {
            return fmt.parse(date);
        } catch (java.text.ParseException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

}

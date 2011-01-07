package com.gwtquickstarter.server;

import java.util.Date;
import java.util.logging.Logger;
import com.googlecode.objectify.*;
import com.googlecode.objectify.helper.DAOBase;
import com.gwtquickstarter.client.util.InvalidLoginException;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class DAO extends DAOBase
{
    private static final Logger log = Logger.getLogger(DAO.class.getName());
    private static final RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

    static {
        ObjectifyService.register(DownloadableAsset.class);
        ObjectifyService.register(ActivationAsset.class);
        ObjectifyService.register(UserData.class);
        ObjectifyService.register(CookieData.class);
        ObjectifyService.register(CachedAjaxLink.class);
    }

    public void removeCookieForUser(String username)
    {
        Query<CookieData> q = ofy().query(CookieData.class).filter("username", username);
        for (CookieData cookie : q)
        {
            log.info("removing cookie");
            ofy().delete(cookie);
        }
    }

    public void updateCookieForUser(String username, String token)
    {
        // remove any existing cookie for user
        removeCookieForUser(username);
        CookieData cookie = new CookieData(username, token);
        ofy().put(cookie);

        log.info("updated cookie");
    }

    public CookieData getCookieDataByUsername(String username)
    {
        return ofy().find(CookieData.class, username);
    }


    public UserData createAccount(String username, String hashedPassword, String salt, String email) throws InvalidLoginException
    {
        UserData result = ofy().find(UserData.class, username);
        if (result != null)
        {
            throw new InvalidLoginException("account \"" + username + "\" already exists");
        }
        
        result = new UserData();
        result.setUsername(username);
        result.setEmail(email);
        result.setHashedPassword(hashedPassword);
        result.setSalt(salt);
        result.setCreationDate(new Date());
        ofy().put(result);

        return result;
    }

    public UserData updatePassword(String username, String hashedPassword, String salt) throws InvalidLoginException
    {
        UserData result = ofy().find(UserData.class, username);
        if (result == null)
        {
            throw new InvalidLoginException("no such account: " + username);
        }

        result.setHashedPassword(hashedPassword);
        result.setSalt(salt);
        ofy().put(result);

        return result;
    }

    public UserData updateEmail(String username, String newEmailAddress) throws InvalidLoginException
    {
        UserData result = ofy().find(UserData.class, username);
        if (result == null)
        {
            throw new InvalidLoginException("no such account: " + username);
        }

        result.setEmail(newEmailAddress);
        ofy().put(result);

        return result;
    }

    public UserData updateLastLogin(String username, Date lastLogin) throws InvalidLoginException
    {
        UserData result = ofy().find(UserData.class, username);
        if (result == null)
        {
            throw new InvalidLoginException("no such account: " + username);
        }

        result.setLastLogin(lastLogin);
        ofy().put(result);

        return result;
    }

    public UserData getByUsername(String username)
    {
        return ofy().get(UserData.class, username);
    }


    public ExpirableAsset createDownloadableAssetUrl(String assetFilePath)
    {
        DownloadableAsset result = new DownloadableAsset();
        result.setKey(randomCodeGenerator.getRandomCode(8));
        result.setFilePath(assetFilePath);
        ofy().put(result);

        return ofy().get(DownloadableAsset.class, result.getKey());
    }

    public void persistAsset(ExpirableAsset asset)
    {
        ofy().put(asset);
    }

    public ExpirableAsset createActivationUrl()
    {
        ActivationAsset result = new ActivationAsset();
        result.setActivationType(ActivationAsset.ActivationType.account);
        result.setKey(randomCodeGenerator.getRandomCode(8));
        ofy().put(result);

        return ofy().get(ActivationAsset.class, result.getKey());
    }

    public ActivationAsset getActivationAssetByKey(String assetKey)
    {
        try
        {
            return ofy().get(ActivationAsset.class, assetKey);
        } catch (NotFoundException e)
        {
            return null;
        }
    }

    public DownloadableAsset getDownloadableAssetByKey(String assetKey)
    {
        try
        {
            return ofy().get(DownloadableAsset.class, assetKey);
        } catch (NotFoundException e)
        {
            return null;
        }
    }


    public ExpirableAsset incrementUses(ExpirableAsset asset)
    {
        // TODO: transaction
        asset.setNumberOfUses(asset.getNumberOfUses() + 1);
        ofy().put(asset);

        log.info("downloads incremented to: " + asset.getNumberOfUses());

        return asset;
    }

    public void updateCachedAjaxLink(CachedAjaxLink cachedLink)
    {
        ofy().put(cachedLink);
    }

    public CachedAjaxLink getCachedAjaxLink(String url)
    {
        return ofy().find(CachedAjaxLink.class, url);
    }


}

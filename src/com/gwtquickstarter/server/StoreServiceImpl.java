package com.gwtquickstarter.server;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwtquickstarter.client.StoreService;
import com.gwtquickstarter.client.model.*;
import com.gwtquickstarter.client.util.*;

@SuppressWarnings({"serial", "GwtServiceNotRegistered"})
/**
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class StoreServiceImpl extends RemoteServiceServlet implements StoreService
{
    protected static final Logger log = Logger.getLogger(StoreServiceImpl.class.getName());
    private static final DAO dao = new DAO();

    public static final String sandBoxCheckoutUrl = "https://sandbox.google.com/checkout/api/checkout/v2/checkoutForm/Merchant/";
    public static final String productionCheckoutUrl = "https://checkout.google.com/api/checkout/v2/checkoutForm/Merchant/";

    public final char[] DISALLOWED_USERNAME_CHARS = { ':' };
    protected String taskQueuePath;
    protected static final Map<String, Product> products = new HashMap<String, Product>();
    protected RandomCodeGenerator randomCodeGenerator = new RandomCodeGenerator();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        taskQueuePath = getServletContext().getInitParameter("taskQueuePath");
    }

    protected Product getProductById(String productId)
    {
        Product result = products.get(productId);
        if (result == null)
        {
            throw new IllegalArgumentException("no such product: " + productId);
        }

        return result;
    }

    protected void addProduct(Product p)
    {
        products.put(p.getId(), p);
    }

    protected String getAppName()
    {
        return System.getProperty("applicationName");
    }

    @Override
    public AppConfiguration getAppConfiguration()
    {
        AppConfiguration result = new AppConfiguration();
        result.setProperty("hasUserAccounts", System.getProperty("hasUserAccounts"));
        return result;
    }


    @Override
    public void createActivationAsset(String key, int maxUses) throws NotLoggedInException {
        ActivationAsset asset = new ActivationAsset();
        asset.setKey(key);
        asset.setMaxUseCount(maxUses);
        asset.setActivationType(ActivationAsset.ActivationType.account);
        dao.persistAsset(asset);
    }

    @Override
    public void sendFeedback(String feedback, String fromEmail)
    {
        String msg = "user: " + fromEmail + " has this feedback: " + feedback;
        log.warning(msg);
        ServerUtils.sendDefaultEmail("feedback on " + getAppName(), msg);
    }

    @Override
    public void addToMailingList(String email)
    {
        String msg = "user: " + email + " is interested in " + getAppName();
        log.info(msg);
        ServerUtils.sendDefaultEmail("interested in " + getAppName(), msg);
    }

    @Override
    public LoginResult loginViaCookie(String cookieContents) throws InvalidLoginException
    {
        log.info("loginViaCookie: " + cookieContents);

        if (ClientUtils.isEmpty(cookieContents))
        {
            throw new InvalidLoginException("illegal cookie format: " + cookieContents);
        }

        String[] parts = cookieContents.split(":", 2);
        if (parts.length != 2)
        {
            throw new InvalidLoginException("illegal cookie format: " + cookieContents);
        }

        String username = parts[0];
        String token = parts[1];
        

        CookieData cookieData = dao.getCookieDataByUsername(username);
        if (cookieData == null)
        {
            log.info("no login cookie");
            throw new InvalidLoginException("no login cookie");
        }

        // token doesn't match; remove it
        if (! cookieData.getToken().equals(token))
        {
            log.warning("cookie doesn't match");
            throw new InvalidLoginException("invalid login cookie");
        }

        LoginResult result = new LoginResult(username, generateAndPersistNewTokenForAuthenticatedUser(username));

        dao.updateLastLogin(username, new Date());

        UserData userData = dao.getByUsername(username);
        result.setAdmin(userData.isAdmin());
        getThreadLocalRequest().getSession().setAttribute("userModel", result);

        log.info("success");
        return result;
    }

    // user is authenticated; update the token
    protected String generateAndPersistNewTokenForAuthenticatedUser(String username)
    {
        SecureRandom random = AccountUtils.getSecureRandom();
        String newToken = Long.toString(random.nextLong());
        dao.updateCookieForUser(username, newToken);
        return username + ":" + newToken;
    }

    @Override
    public LoginResult login(String username, String password, boolean rememberMe) throws InvalidLoginException
    {
        log.info("attempting login with password for user: " + username);

        UserData userData = AccountUtils.authenticate(dao, username, password);
        if (userData == null)
        {
            throw new InvalidLoginException("invalid password");
        }

        String cookieToken = null;

        if (rememberMe)
        {
            cookieToken = generateAndPersistNewTokenForAuthenticatedUser(username);
        }
        else
        {
            dao.removeCookieForUser(username);
        }

        dao.updateLastLogin(username, new Date());

        LoginResult result = new LoginResult(username, cookieToken);
        result.setAdmin(userData.isAdmin());
        getThreadLocalRequest().getSession().setAttribute("userModel", result);

        return result;
    }

    protected LoginResult getUserFromSession()
    {
        return (LoginResult) getThreadLocalRequest().getSession().getAttribute("userModel");
    }

    @Override
    public void logout()
    {
        log.info("logout");

        LoginResult user = getUserFromSession();
        if (user != null)
        {
            getThreadLocalRequest().getSession().invalidate();
            dao.removeCookieForUser(user.getUsername());
        }

        log.info("logout complete");
    }

    @Override
    public void createAccount(String signupCode, String username, String password) throws InvalidAssetException, InvalidLoginException
    {
        ActivationAsset asset = dao.getActivationAssetByKey(signupCode);
        if (asset == null || asset.getActivationType() != ActivationAsset.ActivationType.account)
        {
            throw new InvalidAssetException("invalid signup code: " + signupCode, signupCode);
        }

        if (! asset.hasUsesRemaining())
        {
            throw new InvalidAssetException("account has already been created for signup code: " + signupCode, signupCode);
        }

        for (char c : DISALLOWED_USERNAME_CHARS)
        {
            if (username.contains(Character.toString(c)))
            {
                throw new InvalidLoginException("illegal character in username: " + c);
            }
        }

        // for now, set email -> username
        String email = username;
        AccountUtils.createUser(dao, username, password, email);
        dao.incrementUses(asset);

        if (asset.isNotifyUponAccess())
        {
            ServerUtils.sendDefaultEmail(getAppName() + " account created", getAppName() + " account created: " + email + ", signup key: " + asset.getKey());
        }
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) throws InvalidLoginException {

        LoginResult loginResult = getUserFromSession();
        if (loginResult == null)
        {
            throw new InvalidLoginException("not logged in");
        }

        AccountUtils.changePassword(dao, loginResult.getUsername(), currentPassword, newPassword);
    }

    @Override
    public void resetPassword(String username) throws InvalidLoginException
    {
        UserData userData = dao.getByUsername(username);
        String newPassword = randomCodeGenerator.getRandomCode(12);
        AccountUtils.changePasswordWithoutValidation(dao, username, newPassword);

        log.info("password reset for: " + username);

        String from = System.getProperty("emailFromAddress");
        String to = userData.getEmail();
        String subject = getAppName() + " password reset";
        String message = "Your password on " + getAppName() + " has been reset to the following temporary password: " + newPassword + "\n";
        message += "Please login to " + getApplicationLoginPage() + "  with this temporary password, then visit the Dashboard to set a new password.";
        ServerUtils.sendEmail(from, from, to, to, subject, message);
    }

    public String getApplicationLoginPage()
    {
        return System.getProperty("applicationLoginPage");
    }

    @Override
    public void changeEmail(String newEmailAddress) throws InvalidLoginException {
        LoginResult loginResult = getUserFromSession();
        if (loginResult == null)
        {
            throw new InvalidLoginException("not logged in");
        }

        dao.updateEmail(loginResult.getUsername(), newEmailAddress);
    }

    @Override
    public String buyNow(String merchantId, String productId, int quantity, boolean sandbox)
    {
        Product product = getProductById(productId);

        ExpirableAsset asset;

        if (product instanceof DownloadableProduct)
        {
            DownloadableProduct dp = (DownloadableProduct) product;
            asset = dao.createDownloadableAssetUrl(dp.getAssetPath());
        }
        else
        {
            asset = dao.createActivationUrl();
        }

        String retrievalUrl = resolveExpirableAsset(getThreadLocalRequest(), asset);
        log.info("retrievalUrl: " + retrievalUrl);
        return initiateOrder(merchantId, product, quantity, sandbox, retrievalUrl);
    }

    protected String resolveExpirableAsset(HttpServletRequest req, ExpirableAsset asset)
    {
        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet

        String portAsString = serverPort == 80 ? "" : ":" + serverPort;

        if (asset instanceof DownloadableAsset)
        {
            return scheme + "://" + serverName + portAsString + contextPath + servletPath + "/../download/" + asset.getKey();
        }
        else
        {
            return scheme + "://" + serverName + portAsString + contextPath + "#!createAccount:" + asset.getKey();
        }
    }

    protected String getProdMerchantKey()
    {
        String result = System.getProperty("prod_merchant_key");
        if (ClientUtils.isEmpty(result))
        {
            throw new IllegalArgumentException("you must define a \"prod_merchant_key\" property in appengine-web.xml");
        }

        return result;
    }

    protected String getSandboxMerchantKey()
    {
        String result = System.getProperty("sandbox_merchant_key");
        if (ClientUtils.isEmpty(result))
        {
            throw new IllegalArgumentException("you must define a \"sandbox_merchant_key\" property in appengine-web.xml");
        }

        return result;
    }


    protected String getMerchantBasicAuthEncoded(String merchantId, boolean sandbox)
    {
        String password = sandbox ? getSandboxMerchantKey() : getProdMerchantKey(); 
        return Base64.encode((merchantId + ":" + password).getBytes());
    }


    protected String initiateOrder(String merchantId, Product product, int quantity, boolean sandbox, String retrievalIUrl) {

        String checkoutUrl = sandbox ? sandBoxCheckoutUrl : productionCheckoutUrl;

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(checkoutUrl + merchantId).openConnection();
            conn.addRequestProperty("Content-Type", "application/xml;charset=UTF-8");
            conn.addRequestProperty("Accept", "application/xml;charset=UTF-8 ");
            conn.addRequestProperty("Authorization", "Basic " + getMerchantBasicAuthEncoded(merchantId, sandbox));

            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setDoOutput(true);

            StringBuffer b = new StringBuffer();

            b.append("_type=checkout-shopping-cart");
            b.append("&");

            b.append("item_name_1=").append(encode(product.getId()));
            b.append("&");

            b.append("item_description_1=").append(encode(product.getDescription()));
            b.append("&");

            b.append("item_quantity_1=").append(quantity);
            b.append("&");

            b.append("item_price_1=").append(product.getPrice());
            b.append("&");

            b.append("item_currency_1=").append(product.getCurrency());
            b.append("&");

            b.append("shopping-cart.items.item-1.digital-content.url=").append(encode(retrievalIUrl));
            b.append("&");

            b.append("shopping-cart.items.item-1.digital-content.description=").append(encode("download your content here"));

            log.info("sending: " + b.toString());
            log.info("sending: " + b.toString());

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(b.toString());
            out.flush();

            String redirectUrl = conn.getHeaderField("Location");
            log.info("response code: " + conn.getResponseCode());
            log.info("response msg: " + conn.getResponseMessage());

            log.info("redirectUrl: " + redirectUrl);
            log.info("redirectUrl: " + redirectUrl);

            logResponse(conn.getInputStream());
            out.close();

            return redirectUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    // read up response bytes else submission will hang
    //
    protected void logResponse(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            log.info(decodedString);
            log.info(decodedString);
        }

        in.close();
    }

    @Override
    public void updateAjaxCrawlerCache()
    {
        HttpServletRequest req = getThreadLocalRequest();
        String scheme = req.getScheme();
        String server = req.getServerName();
        int port = req.getServerPort();
        String path = req.getContextPath() + "#!home:";

        try
        {
            URL url = new URL(scheme, server, port, path);
            AjaxCacher cacher = new AjaxCacher(taskQueuePath);
            cacher.queueCrawlRequest(url.toString(), new Date());
        } catch (IOException e)
        {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}

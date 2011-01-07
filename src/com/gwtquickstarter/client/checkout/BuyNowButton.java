package com.gwtquickstarter.client.checkout;

import com.gwtquickstarter.client.*;
import com.gwtquickstarter.client.util.AsyncCall;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class BuyNowButton extends Composite implements ClickHandler
{
    public static final StoreServiceAsync service = GWT.create(StoreService.class);

    protected boolean sandbox = false;
    protected String merchantId;
    protected String productId;

    protected int width = 180, height = 46;
    protected String style = "white";
    protected String variant = "text";
    protected String loc = "en_US";
    protected int quantity = 1;

    protected VerticalPanel vpanel = new VerticalPanel();

    public BuyNowButton()
    {
        initWidget(vpanel);
    }

    @Override
    protected void onAttach()
    {
        super.onAttach();
        Image img = new Image(getBaseImageUrl() + "?merchant_id=" + getMerchantId() + "&w=" + getWidth() + "&h=" + getHeight() + "&style=" + getStyle() + "&variant=" + getVariant() + "&loc=" + getLoc());
        img.setTitle("Fast checkout through Google");

        PushButton pb = new PushButton(img);
        pb.addClickHandler(this);
        vpanel.clear();
        vpanel.add(pb);
    }

    protected String getBaseImageUrl()
    {
        return isSandbox() ? "http://sandbox.google.com/checkout/buttons/checkout.gif" : "http://checkout.google.com/buttons/checkout.gif";
    }

    public void buyNow()
    {
        new AsyncCall<String>("Contacting Google Checkout...", 200) {
            @Override
            protected void callService(AsyncCallback<String> cb)
            {
                service.buyNow(getMerchantId(), getProductId(), getQuantity(), isSandbox(), cb);
            }

            @Override
            public void onSuccess(String redirectUrl)
            {
                Window.Location.assign(redirectUrl);
            }
        }.go();
    }


    @Override
    public void onClick(ClickEvent event)
    {
        buyNow();
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public boolean isSandbox()
    {
        return sandbox;
    }

    public void setSandbox(boolean sandbox)
    {
        this.sandbox = sandbox;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getMerchantId()
    {
        return merchantId;
    }

    public void setMerchantId(String merchantId)
    {
        this.merchantId = merchantId;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getVariant()
    {
        return variant;
    }

    public void setVariant(String variant)
    {
        this.variant = variant;
    }

    public String getLoc()
    {
        return loc;
    }

    public void setLoc(String loc)
    {
        this.loc = loc;
    }

}

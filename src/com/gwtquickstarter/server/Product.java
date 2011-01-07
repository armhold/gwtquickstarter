package com.gwtquickstarter.server;

/**
 * Description of a product.  This is kept server-side, as we don't want to trust
 * sensitive data (e.g. price) coming from the client. Instead, define the Product
 * server-side, and only refer to it by id on the client.
 *
 * @author Copyright (c) 2011 George Armhold
 */
public abstract class Product
{
    private String id;
    private String price;
    private String description;
    private String currency = "USD";

    public Product(String id, String price, String description)
    {
        this.id = id;
        this.price = price;
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public String getPrice()
    {
        return price;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCurrency()
    {
        return currency;
    }
}

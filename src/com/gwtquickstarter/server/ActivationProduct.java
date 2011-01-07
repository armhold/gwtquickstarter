package com.gwtquickstarter.server;

/**
 * A Product that has no downloadable content, but represents a purchasable "activation",
 * e.g. the right to create an account. 
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class ActivationProduct extends Product
{
    public ActivationProduct(String id, String price, String description)
    {
        super(id, price, description);
    }
}

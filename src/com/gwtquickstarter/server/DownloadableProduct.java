package com.gwtquickstarter.server;

/**
 * server-side class for representing a downloadable product
 *
 * @author Copyright (c) 2011 George Armhold
 */
public class DownloadableProduct extends Product
{
    private String assetPath;

    /**
     * @param id the product id
     * @param price the product price
     * @param description the product description
     * @param assetPath the path to the asset file, relative to the "war/assets" directory, e.g. "stuff.zip"
     */
    public DownloadableProduct(String id, String price, String description, String assetPath)
    {
        super(id, price, description);
        this.assetPath = assetPath;
    }

    public String getAssetPath()
    {
        return assetPath;
    }
}

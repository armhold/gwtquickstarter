package com.gwtquickstarter.server;

import java.io.Serializable;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class ActivationAsset extends ExpirableAsset implements Serializable
{
    private static final long serialVersionUID = -7765449577834189532L;

    public enum ActivationType  { account }

    private ActivationType activationType;

    public ActivationAsset()
    {
        setMaxUseCount(1);
        setNotifyUponAccess(true);
    }

    public ActivationType getActivationType()
    {
        return activationType;
    }

    public void setActivationType(ActivationType activationType)
    {
        this.activationType = activationType;
    }

}

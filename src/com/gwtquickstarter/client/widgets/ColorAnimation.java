package com.gwtquickstarter.client.widgets;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;

/**
 * @author Copyright (c) 2011 George Armhold
*/
public class ColorAnimation extends Animation
{
    private int halfTime;
    private String startColor, endColor;
    private final String origColor;
    private Element elementToManipulate;
    private String property;
    private boolean loop;
    private boolean canceled;

    public ColorAnimation(String startColor,
                          String endColor,
                          Element elementToManipulate,
                          String property,
                          int animTime,
                          boolean loop)
    {
        this.startColor = startColor;
        this.endColor = endColor;

        origColor = elementToManipulate.getStyle().getProperty(property);
        this.elementToManipulate = elementToManipulate;
        this.property = property;
        halfTime = animTime / 2;
        this.loop = loop;

        if (! startColor.startsWith("#"))
        {
            throw new IllegalArgumentException("not a valid hex color: " + startColor);
        }

        if (! endColor.startsWith("#"))
        {
            throw new IllegalArgumentException("not a valid hex color: " + endColor);
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onUpdate(double progress)
    {
        String color = between(startColor, endColor, progress);
        elementToManipulate.getStyle().setProperty(property, color);
    }

    @Override
    protected void onComplete()
    {
        super.onComplete();

        String tmp = startColor;
        startColor = endColor;
        endColor = tmp;

        if (loop && ! canceled)
        {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    run(halfTime);
                }
            });
        }

        if (canceled)
        {
            elementToManipulate.getStyle().setProperty(property, origColor);
        }
    }

    public void cancelLoop() {
        canceled = true;
    }

    public void go()
    {
        canceled = false;
        run(halfTime);
    }

    public String between(String c1, String c2, double percent)
    {
        int r1 = getRed(c1);
        int g1 = getGreen(c1);
        int b1 = getBlue(c1);

        int r2 = getRed(c2);
        int g2 = getGreen(c2);
        int b2 = getBlue(c2);

        int r3 = between(r1, r2, percent);
        int g3 = between(g1, g2, percent);
        int b3 = between(b1, b2, percent);

        return "#" + asTwoDigitHex(r3) + asTwoDigitHex(g3) + asTwoDigitHex(b3);
    }


    protected String asTwoDigitHex(int r)
    {
        String s = Integer.toHexString(r);
        if (s.length() == 0)
        {
            s = "00";
        }
        else if (s.length() == 1)
        {
            s = "0" + s;
        }

        return s;
    }

    protected int between(int r1, int r2, double percent)
    {
        if (r1 > r2)
        {
            return r1 - (int) (Math.abs(r1 - r2) * percent);
        }
        else if (r1 < r2)
        {
            return r1 + (int) (Math.abs(r1 - r2) * percent);
        }
        else
        {
            return r1;
        }
    }

    public int getRed(String hexColor)
    {
        return Integer.parseInt(hexColor.substring(1, 3), 16);
    }

    public int getGreen(String hexColor)
    {
        return Integer.parseInt(hexColor.substring(3, 5), 16);
    }

    public int getBlue(String hexColor)
    {
        return Integer.parseInt(hexColor.substring(5, 7), 16);
    }

}

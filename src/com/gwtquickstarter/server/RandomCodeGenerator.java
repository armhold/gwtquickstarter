package com.gwtquickstarter.server;

import java.util.Random;

/**
 * @author Copyright (c) 2011 George Armhold
 */
public class RandomCodeGenerator
{
    protected Random random = new Random(System.currentTimeMillis());

    public RandomCodeGenerator() { }


    public String getRandomCode(int digits)
    {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < digits; i++)
        {
            result.append(getRandomChar());
        }

        return result.toString();
    }

    protected char getRandomChar()
    {
        int i = Math.abs(random.nextInt()) % 25;
        i = i + 'A';
        return (char) i;
    }

}

package com.bogueratcreations.eaftoolkit;

import java.text.DecimalFormat;

/**
 * Created by TheBogueRat on 6/6/2015.
 */
public class convert {
    // General Usage: result = convert.sizeOf("hello");

    /**
     *
     * @param percent Rise divided by Run
     * @return The degrees formatted string
     */
    public static String Percent2Degrees(Double percent) {
        DecimalFormat df = new DecimalFormat("#.##");
        Double radians = Math.atan(percent/100);
        Double degrees = radians * 18000 / Math.PI /100;
        return df.format(degrees);
    }

    /**
     *
     * @param degrees
     * @return The percent slope equivalent formatted string.
     */
    public static String Degrees2Percent(Double degrees) {
        //
        DecimalFormat df = new DecimalFormat("#.##");
        Double radians = Math.toRadians(degrees);
        Double percent = Math.tan(radians) * 100.0;
        return df.format(percent);
    }
}

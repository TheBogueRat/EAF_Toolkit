package com.bogueratcreations.eaftoolkit.slope;

import java.text.DecimalFormat;

/**
 * Created by jodyroth on 10/21/16.
 */

class SlopeConversions {
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
     * @param degrees Input to convert
     * @return The percent slope equivalent formatted string.
     */
    public static String Degrees2Percent(Double degrees) {
        //
        Double radians = Math.toRadians(degrees);
        Double percent = Math.tan(radians) * 100.0;
        percent = Math.round(percent*100.0)/100.0;
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(percent);
    }
}

package com.bogueratcreations.eaftoolkit;

import java.text.DecimalFormat;
import java.util.Calendar;

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

    /**
     *
     * @param cal A Calendar Object with date to be converted
     * @return Julian Date string format "YDDD"
     */
    public static String cal2Julian(Calendar cal) {
        String julian;
        String yStr;
        String dStr;

        yStr = String.valueOf(cal.get(Calendar.YEAR));
        yStr = yStr.substring(yStr.length() - 1);
        dStr = String.format("%03d", cal.get(Calendar.DAY_OF_YEAR));

        julian = yStr + dStr;

        return julian;
    }
}


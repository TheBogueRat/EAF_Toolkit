package com.bogueratcreations.eaftoolkit.DCP.model;

import android.util.Log;
import android.widget.Switch;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jodyroth on 10/21/16.
 *
 * Reading Class contains DCP Readings
 */

public class Reading extends RealmObject{

    @PrimaryKey
    private long id;
    private int readingNum;
    private int hammer = 0;
    private int blows = 0;
    private int depth = 0;
    private int soilType = 0; // Only needed to calc CBR, derived from Point (maybe just use that.)
    private double cbr = 0.0;
    private int totalDepth;

    // Doesn't need the Point? Since this will be a part of the Point object? (NOT MAKING SENSE)
    private Point point;

    public long getId() {
        return id;
    }

    public void  setId(long id) {
        this.id = id;
    }

    public int getReadingNum() {
        return readingNum;
    }

    public void setReadingNum(int readingNum) {
        this.readingNum = readingNum;
    }

    public int getHammer() {
        return hammer;
    }

    public void setHammer(int hammer) {
        this.hammer = hammer;
    }

    public int getBlows() {
        return blows;
    }

    public void setBlows(int blows) {
        this.blows = blows;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getSoilType() {
        return soilType;
    }

    public void setSoilType(int soilType) {
        this.soilType = soilType;
    }

    public double getCbr() {
        return cbr;
    }

    public void setCbr(double cbr) {
        this.cbr = cbr;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getTotalDepth() {
        return totalDepth;
    }

    public void setTotalDepth(int totalDepth) {
        this.totalDepth = totalDepth;
    }

    // Only call after depth, blows, hammer, and soilType are initialized
    public void calcCbr() {
        // Check for valid values
        if ((blows == 0) || (hammer == 0) || (depth == 0)) {
            // Abort with 0.0 but should only happen on the first entry because 0 blows is forced.
            this.cbr = 0.0;
            return;
        }
        // Calculates the CBR from other parameters already in this object
        Double theCBR = 0.0;
        Double perBlow = ((double)depth / (double)blows * (double)hammer);
        // Calculates CBR rounded to tenths and returns 100.0 or less.
        switch (soilType) {

            case 0:
                // Low Plasticity Clay
                theCBR = (Math.round(1.0 / (Math.pow(0.017019 * perBlow, 2.0)) * 10.0) / 10.0);
                break;
            case 1:
                // High Plasticity Clay
                theCBR =(Math.round((1.0 / (0.002871 * perBlow)) * 10.0) / 10.0);
                break;
            case 2:
                // All Other Soils
                theCBR = (Math.round((292.0 / Math.pow(perBlow, 1.12)) * 10.0) / 10.0);

                break;
            default:
                break;
        }
        Log.d("EAFToolkit", "perBlow: " + String.valueOf(perBlow) + "  Calculated CBR: " + String.valueOf(theCBR));
        if (theCBR > 100.0) {theCBR = 100.0;}
        this.cbr =  theCBR;
    }
}

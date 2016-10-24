package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jodyroth on 10/21/16.
 */

public class Point extends RealmObject {

    private int pointNum;
    private int hammer;
    private int blows;
    private int depth;
    private int soilType;
    private double cbr;
    private Date date;
    private RealmList<Reading> readings;

    void cascadeDeleteReadings() {
        readings.deleteAllFromRealm(); // The cascade part

        // TODO: NOT SURE THIS SHOULD BE IN THE FINAL OBJECT
        //deleteFromRealm(); // delete this object
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<Reading> getReadings() {
        return readings;
    }

    public void setReadings(RealmList<Reading> readings) {
        this.readings = readings;
    }

}

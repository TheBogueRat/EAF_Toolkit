package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jodyroth on 10/21/16.
 */

public class Point extends RealmObject {

    private int pointNum;
    private int soilType;
    private double cbr; // This is the minimum CBR from the readings.
    private Date date;
    private RealmList<Reading> readings;

    void cascadeDeletePoint() {
        readings.deleteAllFromRealm(); // The cascade part
        deleteFromRealm(); // delete this object
    }

    void cascadeDeleteReadings() {
        readings.deleteAllFromRealm(); // The cascade part
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
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

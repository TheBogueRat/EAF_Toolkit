package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jodyroth on 10/21/16.
 */

public class Point extends RealmObject {

    @PrimaryKey
    private long id;
    private String pointNum;
    private int soilType;
    private double cbr; // This is the minimum CBR from the readings.
    @Required
    private Date date;
    // The project I belong to...
    private Project project;
    // The Readings for this point...
    private RealmList<Reading> readings;

    public void cascadeDeletePoint() {
        readings.deleteAllFromRealm(); // The cascade part
        deleteFromRealm(); // delete this object
    }

    public void cascadeDeleteReadings() {
        readings.deleteAllFromRealm(); // The cascade part
    }

    public long getId() {
        return id;
    }

    public void  setId(long id) {
        this.id = id;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
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

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public RealmList<Reading> getReadings() {
        return readings;
    }

    public void setReadings(RealmList<Reading> readings) {
        this.readings = readings;
    }

}

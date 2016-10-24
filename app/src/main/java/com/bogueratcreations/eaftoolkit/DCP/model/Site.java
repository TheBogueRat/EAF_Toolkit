package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jodyroth on 10/24/16.
 */

public class Site extends RealmObject {

    private String nameLoc;
    private int soilType;
    private String soilInfo;
    private Date date;
    private float latitude;
    private float longitude;
    private RealmList<Point> points;

    void cascadeDeletePoints() { // Remove points and readings but leave the Site, called from Project
        // Removed referenced readings first.
        for (Point point : points) {
            point.cascadeDeleteReadings();  // Cascade delete readings
        }
        points.deleteAllFromRealm(); // Cascade delete points

        // TODO: NOT SURE THIS SHOULD BE IN THE FINAL OBJECT
        // I don't think I want to delete this here, calling cascade delete from higher object
        // deleteFromRealm(); // delete this object
    }

    public String getNameLoc() {
        return nameLoc;
    }

    public void setNameLoc(String nameLoc) {
        this.nameLoc = nameLoc;
    }

    public int getSoilType() {
        return soilType;
    }

    public void setSoilType(int soilType) {
        this.soilType = soilType;
    }

    public String getSoilInfo() {
        return soilInfo;
    }

    public void setSoilInfo(String soilInfo) {
        this.soilInfo = soilInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public RealmList<Point> getPoints() {
        return points;
    }

    public void setPoints(RealmList<Point> points) {
        this.points = points;
    }

}

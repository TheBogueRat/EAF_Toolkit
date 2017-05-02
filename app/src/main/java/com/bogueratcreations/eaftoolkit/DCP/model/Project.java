package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jodyroth on 10/24/16.
 */

public class Project extends RealmObject {
    //TODO: Add required tags to appropriate fields.
    @PrimaryKey
    private long id;
    @Required
    private String projName;
    private String projLoc;
    private String projInfo;
    @Required
    private Date dateCreated;
    private int soilType;
    private String soilInfo;
    private double latitude;
    private double longitude;
    private RealmList<Point> points;

    public void cascadeDeleteProject() {
        for (Point point : points) {
            point.cascadeDeleteReadings(); // removes readings
        }
        points.deleteAllFromRealm(); // removes points
        deleteFromRealm(); // removes self
    }

    public long getId() {
        return id;
    }

    public void  setId(long id) {
        this.id = id;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjLoc() {
        return projLoc;
    }

    public void setProjLoc(String projLoc) {
        this.projLoc = projLoc;
    }

    public String getProjInfo() {
        return projInfo;
    }

    public void setProjInfo(String projInfo) {
        this.projInfo = projInfo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public RealmList<Point> getPoints() {
        return points;
    }

    public void setPoints(RealmList<Point> points) {
        this.points = points;
    }


}

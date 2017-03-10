package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jodyroth on 10/21/16.
 */

public class Reading extends RealmObject{

    @PrimaryKey
    private long id;
    private int readingNum;
    private int hammer;
    private int blows;
    private int depth;
    private int soilType; // Only needed to calc CBR, derived from Point
    private double cbr;
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
}

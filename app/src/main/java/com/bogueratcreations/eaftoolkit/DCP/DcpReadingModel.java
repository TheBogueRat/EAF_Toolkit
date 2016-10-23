package com.bogueratcreations.eaftoolkit.DCP;

import io.realm.RealmObject;

/**
 * Created by jodyroth on 10/21/16.
 */

public class DcpReadingModel extends RealmObject{
    private int readingNum;
    private int hammer;
    private int blows;
    private int depth;
    private int soilType;
    private double cbr;

    public double getCbr() {
        return cbr;
    }

    public void setCbr(double cbr) {
        this.cbr = cbr;
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

}

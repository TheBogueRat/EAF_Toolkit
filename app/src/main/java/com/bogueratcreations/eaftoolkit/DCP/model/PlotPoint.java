package com.bogueratcreations.eaftoolkit.DCP.model;

/**
 * Created by jodyroth on 4/5/17.
 */

public class PlotPoint {
    // The x,y coordinate
    private float mX;
    private float mY;

    public PlotPoint() { this(0,0);}

    public PlotPoint(double x, double y) {
        // casts doubles to float if necessary
        this((float)x, (float)y);
    }

    public PlotPoint(float mX, float mY) {
        this.mX = mX;
        this.mY = mY;
    }

    public float getX() {
        return mX;
    }

    public void setX(float mX) {
        this.mX = mX;
    }

    public void setX(double x) {
        this.mX = (float) x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float mY) {
        this.mY = mY;
    }

    public void setY(double y) {
        this.mY = (float) y;
    }

    @Override
    public String toString() {
        return "x = " + mX + ", y = " + mY;
    }

}

package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.ArrayList;

/**
 * Created by jodyroth on 4/5/17.
 */

public class PlotSeries {
    // Contains a list of points and a name for the series.
    private ArrayList<PlotPoint> mPlotPoints = new ArrayList<>();
    private String mSeriesName;

    public ArrayList<PlotPoint> getPlotPoints() {
        return mPlotPoints;
    }

    public String getSeriesName() {
        return mSeriesName;
    }

    public void setSeriesName(String seriesName) {
        this.mSeriesName = seriesName;
    }

    public void setPlotPoint(ArrayList<PlotPoint> plotPoints) {
        this.mPlotPoints = plotPoints;
    }

    public void addPlotPoint(PlotPoint plotPoint) {
        mPlotPoints.add(plotPoint);
    }

    public int getSize() {
        return mPlotPoints.size();
    }
}

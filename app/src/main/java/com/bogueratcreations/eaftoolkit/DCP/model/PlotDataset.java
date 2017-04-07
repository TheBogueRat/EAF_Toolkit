package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.ArrayList;

/**
 * Created by jodyroth on 4/5/17.
 */

public class PlotDataset {

    private ArrayList<PlotSeries> mPlotDataset = new ArrayList<>();
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ArrayList<PlotSeries> getPlotDataset() {
        return mPlotDataset;
    }

    public void setPlotDataset(ArrayList<PlotSeries> mPlotSeries) {
        this.mPlotDataset = mPlotSeries;
    }

    public void addSeries(PlotSeries plotSeries) {
        this.mPlotDataset.add(plotSeries);
    }

    public int getSize() {
        return mPlotDataset.size();
    }
}

package com.bogueratcreations.eaftoolkit.DCP;

/**
 * Created by jodyroth on 4/4/17.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

import com.bogueratcreations.eaftoolkit.DCP.model.PlotDataset;
import com.bogueratcreations.eaftoolkit.DCP.model.PlotPoint;
import com.bogueratcreations.eaftoolkit.DCP.model.PlotSeries;

public class GraphView extends View {

    private Paint paint;
    private Paint paintBg;
    private Paint paintBorder;
    private Paint paintSubDivs;
    private Paint paintLabel;
    private Paint paintLine;
    private Paint paintLineSecondary;

    private PlotDataset mPlotDataset;
    private String[] horlabels;
    private String[] verlabels;
    private float lMargin = 100;
    private float rMargin = 100;
    private float tMargin = 75;
    private float bMargin = 125; // Adjust for legend.
    private float legendHeight = 100;
    private float[] horizontalIndex = new float[]{2f,3f,4f,5f,6f,7f,8f,9f,10f,20f,30f,40f,50f,60f,70f,80f,90f};
    private float[] verticalIndex = new float[]{0f,5f,10f,15f,20f,25f,30f,35f,40f};
    private int plotDatasetLength;
    private int[] colors = new int[]{
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };

    public GraphView(Context context, PlotDataset plotDataSet, String[] horlabels, String[] verlabels) {
        super(context);
        if (plotDataSet == null)
            return;
        else
            this.mPlotDataset = plotDataSet;
        if (horlabels == null)
            this.horlabels = new String[0];
        else
            this.horlabels = horlabels;
        if (verlabels == null)
            this.verlabels = new String[0];
        else
            this.verlabels = verlabels;
        paint = new Paint();
        paintBg = new Paint();
        paintBg.setColor(Color.WHITE);
        paintBorder = new Paint();
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStrokeWidth(4f);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintSubDivs = new Paint();
        paintSubDivs.setColor(Color.LTGRAY);
        paintSubDivs.setStrokeWidth(2f);
        paintLabel = new Paint();
        paintLabel.setColor(Color.BLACK);
        paintLine = new Paint();
        paintLine.setStrokeWidth(4f);
        paintLineSecondary = new Paint();
        paintLineSecondary.setAlpha(127);
        paintLineSecondary.setStrokeWidth(2f);
        plotDatasetLength = plotDataSet.getSize();
        legendHeight = (float)(40 * Math.ceil(plotDatasetLength / 2) + 40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float border = 50; // left & top are working
        float horstart = lMargin;  // Removed * 2
        float height = getHeight();
        float width = getWidth() - 1;
        float xScale = 100; // Width of chart
        float yScale = 1020; // Height of chart
        float max = 100; // getMax();  // Max height of chart
        float min = 0; // getMin();  // Min height of chart
        float diff = max - min;
        float graphHeight = height - tMargin - bMargin - legendHeight; // Minus size of legend...
        float graphWidth = width - lMargin - rMargin;

        // Draw a full screen white background so exported graph looks right.
        canvas.drawRect(0,0,width,height,paintBg);

        // Draw secondary increments (without labels)
        for (float i : horizontalIndex) {
            float x = (scaleLog(i) / xScale) * graphWidth + lMargin;
            canvas.drawLine(x, tMargin + graphHeight, x, tMargin, paintSubDivs);
        }
        paint.setTextSize(24f);
        paint.setColor(Color.DKGRAY);
        for (float i: verticalIndex) {
            float y = (float)((i*25.4) / yScale * graphHeight) + tMargin;  //(xVal / xScale) * graphWidth + lMargin
            canvas.drawLine(lMargin, y, lMargin + graphWidth, y, paintSubDivs);
            paint.setTextAlign(Align.RIGHT);
            canvas.drawText(String.valueOf((int)i), lMargin - 16, y + 10, paint);
            paint.setTextAlign(Align.LEFT);
            canvas.drawText(String.valueOf((int)(i*25.4)), lMargin + graphWidth + 16, y + 10, paint);
        }

        int hors = horlabels.length - 1;
        for (int i = 0; i < horlabels.length; i++) {
            paint.setColor(Color.DKGRAY);
            float x = ((graphWidth / hors) * i) + lMargin;
            paint.setTextAlign(Align.CENTER);
            if (i==horlabels.length-1)
                paint.setTextAlign(Align.RIGHT);
            if (i==0)
                paint.setTextAlign(Align.LEFT);
            paint.setColor(Color.DKGRAY);
            // Draw the vertial lines indicating CBR
            canvas.drawText(horlabels[i], x, graphHeight + tMargin + 30, paint);
        }

        // Draw graph bounding box, drawing after the gradient lines to ensure it's on top.
        canvas.drawRect(lMargin, tMargin, lMargin + graphWidth, tMargin + graphHeight, paintBorder);

        // Put up the Title.
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(36f);
        canvas.drawText(mPlotDataset.getTitle(), (graphWidth / 2) + lMargin, tMargin - 24, paint);
        paint.setTextSize(30f);
        canvas.drawText("CBR", (graphWidth/2) + lMargin, tMargin + graphHeight + 64, paint);
        // Draw Axis Label on Left
        int fontHeight = 30; // I am (currently) too lazy to properly request the fontHeight and it does not matter for this example :P
        int y = (int)(tMargin + (graphHeight/2) - (30 * 15)/2);
        for(char c: "DEPTH IN INCHES".toCharArray()) {
            canvas.drawText(String.valueOf(c), 25, y, paint);
            y += fontHeight;
        }
        y = (int)(tMargin + (graphHeight/2) - (30 * 11)/2);
        for(char c: "MILLIMETERS".toCharArray()) {
            canvas.drawText(String.valueOf(c), width - 25, y, paint);
            y += fontHeight;
        }
        int legendXoffset = 0;
        int legendYoffset = 0;
        int colorIndex = 0;
        // Drawing the line
        for(PlotSeries s : mPlotDataset.getPlotDataset()) {
            // Start each series at origin.
            float lastX = 0 + lMargin;
            float lastY = 0 + tMargin;
            boolean firstPoint = true;
            paintLine.setColor(colors[colorIndex]);
            paint.setColor(colors[colorIndex]);
            paintLineSecondary.setColor(colors[colorIndex]);

            for(PlotPoint p : s.getPlotPoints()) {
                float xVal = (float)(Math.log(p.getX()) / Math.log(100)) * 100;
                float yVal = p.getY();

                // Calc coordinates scaled to this graph
                float xPlot = (xVal / xScale) * graphWidth + lMargin;
                float yPlot = (yVal / yScale) * graphHeight + tMargin;
                // Draw horizontally from last position.
                if (firstPoint) {
                    // Skip first plot.
                    firstPoint = false;
                } else {
                    // Draw lighter lines direct to point.
                    canvas.drawLine(lastX,lastY,xPlot,yPlot,paintLineSecondary);
//                    // Draw stepped lines.
//                    canvas.drawLine(lastX, lastY, xPlot, lastY, paintLine);
//                    // Draw vertically for current depth.
//                    canvas.drawLine(xPlot, lastY, xPlot, yPlot, paintLine);
                }
                // Always plot the point.
                canvas.drawCircle(xPlot, yPlot, 6f, paintLine);
                lastX = xPlot;
                lastY = yPlot;
            }
            // Plot the legend data.
            paint.setTextAlign(Align.LEFT);
            int nameLength = (int)paint.measureText(s.getSeriesName());
            if ((nameLength + legendXoffset + 100) > width) {
                legendYoffset = legendYoffset + 40;
                legendXoffset = 0;
            }
            canvas.drawText(s.getSeriesName(), 50 + legendXoffset, tMargin + graphHeight + bMargin + legendYoffset, paint);
            legendXoffset = legendXoffset + nameLength + 50;

            if (colorIndex == colors.length) {
                colorIndex = 0;
            } else {
                colorIndex++;
            }
        }
    }

    private float scaleLog(float toScale) {
        return (float)(Math.log(toScale) / Math.log(100)) * 100;
    }
}

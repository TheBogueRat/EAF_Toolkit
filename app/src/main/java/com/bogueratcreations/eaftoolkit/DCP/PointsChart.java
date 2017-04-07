package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bogueratcreations.eaftoolkit.R;

import io.realm.Realm;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;

public class PointsChart extends AppCompatActivity {

    long passedProjectID;

    private Realm realm;
    Project passedProject;

//    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        passedProjectID = getIntent().getLongExtra("projId",-1);
//        realm = Realm.getDefaultInstance();
//        passedProject = realm.where(Project.class)
//                .equalTo("id",passedProjectID)
//                .findFirst();
//
//        // Customize the Chart
//        chart = (LineChart) findViewById(R.id.chartProject);
//        chart.setKeepPositionOnRotation(true);
//        chart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
//        chart.setHighlightPerTapEnabled(false);
//        chart.setHighlightPerDragEnabled(false);
//        chart.setNoDataText("No data to display.");
//        chart.getDescription().setText(passedProject.getProjName());
//        Legend legend = chart.getLegend();
//        legend.setWordWrapEnabled(true);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setAxisMinimum(1f);
//        xAxis.setAxisMaximum(101f);
//        xAxis.setLabelCount(8);
//        xAxis.setDrawLabels(true);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                DecimalFormat mFormat;
//                mFormat = new DecimalFormat("##0.#"); // use one decimal.
//                if (value <= 0) {
//                    return "0";
//                } else {
//                    return mFormat.format(unScaleCbr(value));
//                }
//            }
//        });
//        YAxis yAxis = chart.getAxisLeft();
//        yAxis.setInverted(true);
//        yAxis.setAxisMaximum(1020f);
//        yAxis.setAxisMinimum(0f);
//        YAxis yAxis1 = chart.getAxisRight();
//        yAxis1.setInverted(true);
//        yAxis1.setAxisMaximum(1020f);
//        yAxis1.setAxisMinimum(0f);
//        yAxis1.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                DecimalFormat mFormat;
//                mFormat = new DecimalFormat("##0"); // use one decimal
//                return mFormat.format(value/25.4);
//            }
//        });
//
//        // Retrieve applicable readings from the realm...Now to use them in chart.
//        Long[] pointIds = new Long[passedProject.getPoints().size()];
//        int counter = 0;
//        // TODO: Get me some better colors!
//        int[] colors = new int[]{
//                Color.RED,
//                Color.BLUE,
//                Color.GREEN
//        };
//        // use the interface ILineDataSet
//        String seriesTitle[] = new String[passedProject.getPoints().size()];
//        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//
//        for (Point p : passedProject.getPoints()) {
//            // Reinitialize the series for each point.
//            List<Entry> series = new ArrayList<Entry>();
//            String title = p.getPointNum();
//            Log.d("MPChartDataset", "LineDataSet: " + title);
//            for (Reading r : p.getReadings().sort("readingNum")) {
//                // Store x,y values for this series.
//                double cbrMax = r.getCbr();
//                if (cbrMax > 100.0) {cbrMax = 100.0;}
////                Log.d("MPChartDataset", "X: " + scaleCbr(cbrMax) + " Y: " + r.getTotalDepth());
//                Log.d("EAFToolkit", "Blows: " + r.getBlows() + " Depth: " + r.getDepth());
////                Log.d("EAFToolkit", "Reading: " + r.getReadingNum() + " r.getCbr(): " + cbrMax + "  scaleCbr(cbrMax: " + scaleCbr(cbrMax) + " total depth: " + r.getTotalDepth());
//                Entry entry = new Entry(scaleCbr(cbrMax),(float)(r.getTotalDepth()));
//                series.add(entry);
//            }
//            // Find out if there are any readings and force an empty one to prevent crash.
//            if (p.getReadings().size() > 0) {
//                LineDataSet seriesDataSet = new LineDataSet(series, title);
//                seriesDataSet.setColor(colors[counter]);
//                seriesDataSet.setCircleColor(colors[counter]);
//                dataSets.add(seriesDataSet);
//                if (counter == 2) {
//                    counter = 0;
//                } else {
//                    counter++;
//                }
//            }
//        }
//
//        LineData data = new LineData(dataSets);
//        Log.d("EAFToolkit", "LineData: " + dataSets.toString());
//        data.setDrawValues(false);
//        chart.setData(data);
//        chart.invalidate();
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String fileName = passedProject.getProjName() + "_" + (int)(Math.random() * 100);
//                chart.saveToGallery(fileName, 100);
//                Toast.makeText(getBaseContext(), "Saved this graph to your Gallery", Toast.LENGTH_LONG).show();
//            }
//        });
        // Will crash if I do, removing for consistency...
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public float scaleCbr(double cbr) {
        return (float)(Math.log(cbr) / Math.log(100)) * 100;
    }

    public float unScaleCbr(double cbr) {
        double calcVal = Math.pow(10,(cbr/50));
        return (float)(calcVal);
    }

    @Override
    public void onBackPressed() {
        // TODO: Update the lowestCBR for Point here or in onItemSwipedDismiss
        Intent intent = new Intent();
        intent.putExtra("projId", passedProjectID);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }
}

package com.bogueratcreations.eaftoolkit.DCP;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.DCP.model.PlotDataset;
import com.bogueratcreations.eaftoolkit.DCP.model.PlotPoint;
import com.bogueratcreations.eaftoolkit.DCP.model.PlotSeries;
import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import io.realm.Realm;

public class PointsPlot extends AppCompatActivity {

    long passedProjectID;

    private Realm realm;
    Project passedProject;

    GraphView graphView;

    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 25;
    boolean hasPermissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_plot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("CBR Graph");

        // Get data from Realm
        passedProjectID = getIntent().getLongExtra("projId",-1);
        realm = Realm.getDefaultInstance();
        passedProject = realm.where(Project.class)
                .equalTo("id",passedProjectID)
                .findFirst();

        // Insert data into Plot Data Set.
        PlotDataset plotDataset = new PlotDataset();
        plotDataset.setTitle(passedProject.getProjName());

        for (Point p : passedProject.getPoints()) {
            // Reinitialize the series for each point.
            PlotSeries plotSeries = new PlotSeries();
            plotSeries.setSeriesName(p.getPointNum());
            //String title = p.getPointNum();
            for (Reading r : p.getReadings().sort("readingNum")) {
                // Store x,y values for this series.
                double cbrMax = r.getCbr();
                if (cbrMax > 100.0) {cbrMax = 100.0;}
                if (r.getTotalDepth() < 1021) {
                    PlotPoint plotPoint = new PlotPoint(cbrMax, (float) (r.getTotalDepth()));
                    plotSeries.addPlotPoint(plotPoint);
                }
            }
            // Find out if there are any readings and force an empty one to prevent crash.
            if (p.getReadings().size() > 0) {
                plotDataset.addSeries(plotSeries);
            }
        }

        // Plot data
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.plotViewLayout);

        String[] verlabels = new String[] { "0", "510", "1120" };
        String[] horlabels = new String[] { "1", "10", "100" };
        graphView = new GraphView(this, plotDataset, horlabels, verlabels);
        constraintLayout.addView(graphView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SimpleDateFormat formatter = new SimpleDateFormat("d MMM yy_HHmm");

                String fileName = passedProject.getProjName();
//                fileName = fileName.replaceAll(" ","_");
                saveToGallery(fileName, 100);
            }
        });
        // Will crash if I do, removing for consistency...
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveToGallery(String fileName, int quality) {
        // Make sure we have permission
        checkWritePermissions();
        if (hasPermissions) {
            // Get image to save.
            graphView.setDrawingCacheEnabled(true);
            Bitmap source = graphView.getDrawingCache();

        /**
         * A copy of the Android internals insertImage method, this method populates the
         * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
         * that is inserted manually gets saved at the end of the gallery (because date is not populated).
         */

            // Pump it out to the gallery.
//            long dateString = (long)(new Date());
            long dateTime = System.currentTimeMillis() / 1000;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, fileName);
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "EAF Toolkit DCP Project Graph");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, dateTime);
            contentValues.put(MediaStore.Images.Media.DATE_ADDED, dateTime);
            contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, dateTime);

            Uri url = null;
            ContentResolver cr = this.getContentResolver();
//            String stringUrl = null;    /* value to be returned */

            try {
                url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                if (source != null) {
                    OutputStream imageOut = cr.openOutputStream(url);
                    try {
                        source.compress(Bitmap.CompressFormat.JPEG, quality, imageOut);
                    } finally {
                        imageOut.close();
                    }

                    long id = ContentUris.parseId(url);
                    // Wait until MINI_KIND thumbnail is generated.
                    Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                    // This is for backward compatibility.
                    storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
                } else {
                    cr.delete(url, null, null);
//                    return storeToAlternateSd(source, title);
                    // url = null;
                }
            } catch (Exception e) {
                if (url != null) {
                    cr.delete(url, null, null);
//                    return storeToAlternateSd(source, title);
                    // url = null;
                }
            }

//            MediaStore.Images.Media.insertImage(this.getContentResolver(), source, fileName, "EAF Toolkit DCP Project Graph");
            Toast.makeText(getBaseContext(), "Saved this graph to your Gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Please give permission to save the image.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method).
     */
    private Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            Log.e("IMAGE_COMPRESSION_ERROR", "File not found");
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            Log.e("IMAGE_COMPRESSION_ERROR", "IO Exception");
            ex.printStackTrace();
            return null;
        }
    }
    private void checkWritePermissions() {
        // Checks for write permissions to external cache directory.
        // Need to check version because of changes in permissions handling.
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Try to request permissions again without a message.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }else{
                // Have permission.
                hasPermissions = true;
                return;
            }
        }else {
            hasPermissions = true;
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                    return;
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(),"Oops! Image cannot be saved without the appropriate permissions.", Toast.LENGTH_SHORT).show();
                    //btnExport.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
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

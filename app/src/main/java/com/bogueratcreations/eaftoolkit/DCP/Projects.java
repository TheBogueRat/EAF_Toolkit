package com.bogueratcreations.eaftoolkit.DCP;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Projects extends AppCompatActivity {

    // TODO: Display soil type in projects list.

    // Email variables
    private FileWriter writer;

    // Database variables
    private Realm realm;
    RealmResults<Project> projects;

    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 25;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 27;

    boolean hasPermissions = false;
    Button btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(Projects.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                hasPermissions = true;
            }
        }
        realm = Realm.getDefaultInstance();

        projects = realm.where(Project.class).findAll();
        Log.e("Projects content:" , projects.toString());
        final ProjectsListAdapter adapter = new ProjectsListAdapter(this, projects);

        ListView listView = (ListView) findViewById(R.id.listViewProjects);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // Show selected project in edit view in ProjectsAdd activity.
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long projectId = adapter.getItem(i).getId();
                Log.d("EAFToolkit_Debug","Passing this Project id: " + projectId);
                Intent intent = new Intent(view.getContext(), ProjectsAdd.class);
                intent.putExtra("projectId",projectId);
                startActivity(intent);

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Show the selected point in new activity.
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // Pass the selected Point and navigate to the next
                final long projectId = adapter.getItem(position).getId();
                Intent intent = new Intent(view.getContext(), Points.class);
                intent.putExtra("projId", projectId);
                Log.e("EAFToolkit","Passing this Project Id number: " + projectId);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnExport = (Button) findViewById(R.id.btnEmail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProjectsAdd.class);
                intent.putExtra("projectId",-1);
                startActivity(intent);
//                startActivity(new Intent(Projects.this, ProjectsAdd.class));

            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void clickHandlerEmailProjects(View view) {
        checkWritePermissions();
        if (projects.size() > 0) {
            if (hasPermissions) {
                File[] filesToSend = createFilesFromProject();
                sendIntentToEmailApp(filesToSend);
            } else {
                //Toast.makeText(getBaseContext(), "Please give permission to create the attachment.", Toast.LENGTH_SHORT).show();
                checkWritePermissions();
            }
        } else {
            Toast.makeText(getBaseContext(), "No projects to export!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }

    private void checkWritePermissions() {
        // Checks for write permissions to external cache directory.
        // Need to check version because of changes in permissions handling.
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(Projects.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Projects.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Try to request permissions again without a message.
                    ActivityCompat.requestPermissions(Projects.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Projects.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }else{
                // Have permission.
                hasPermissions = true;
            }
        }else {
            hasPermissions = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                    return;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(),"Oops! Project cannot be exported without the appropriate permissions.", Toast.LENGTH_SHORT).show();
                    //btnExport.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    // Should only be called if one or more projects exist!
    private File[] createFilesFromProject() {
        File[] fileArray = new File[projects.size()];
        int index = 0;

        for (Project project : projects) {
            String data = "If this file was converted to 'plain text', save it to your computer with the .csv extension to easily open it with Excel\n\n";
            File file;
            Context context = getApplicationContext();
            file = new File(context.getExternalCacheDir(), project.getProjName());

            data = data + "Project:," + project.getProjName() + "\n";
            data = data + "Date Created:," +project.getDateCreated() + "\n";
            data = data + "Location:," + project.getProjLoc() + "\n";
            data = data + "GPS (not implemented):,Lat:," + project.getLatitude() + ",Long:," + project.getLongitude() + "\n";
            data = data + "Add'l Info:," + project.getProjInfo() + "\n";
            // Display Soil Type.
            switch (project.getSoilType()) {
                case 0:
                    data = data + "Soil Type:,Low Plasticity Clay (under 10 CBR)\n";
                    break;
                case 1:
                    data = data + "Soil Type:,High Plasticity Clay\n";
                    break;
                case 2:
                    data = data + "Soil Type:,All Other Soils\n";
                    break;

            }
            data = data + "Soil Info:," +project.getSoilInfo() + "\n";
            data = data + "\n";
            data = data + ",Number of Points:," + project.getPoints().size() + "\n";
            for (Point point : project.getPoints()) {
                data = data + ",Point:," + point.getPointNum() + "\n";
                data = data + ",,#,Blows,Depth(mm),Hammer,CBR,Depth(in)\n";
                boolean firstReading = true;
                for (Reading reading : point.getReadings().sort("readingNum")) {
                    int totalDepth = reading.getTotalDepth();
                    DecimalFormat formatter = new DecimalFormat("###.#");
                    String totalDepthStr = formatter.format(totalDepth / 25.4);
                    Log.d("EAFToolkit", "Total Depth in Inches: " + totalDepthStr + "  Total Depth: " + totalDepth);
                    if (firstReading) {
                        // Skip blows and CBR for first reading (zeroing)
                        data = data + ",," +
                                reading.getReadingNum() + "," +
                                "0," +
                                totalDepth + "," +
                                reading.getHammer() + "," +
                                "n/a," +
                                totalDepthStr +
                                "\n";
                    } else {
                        data = data + ",," +
                                reading.getReadingNum() + "," +
                                reading.getBlows() + "," +
                                totalDepth + "," +
                                reading.getHammer() + "," +
                                reading.getCbr() + "," +
                                totalDepthStr +
                                "\n";
                    }
                }
                // Separation between points
                data = data + "\n\n";
            }
            try {
                writer = new FileWriter(file);
                writer.write(data);
                writer.close();
            } catch (IOException e) {
                Log.d("EAFToolkit", "Unable create file. Check logcat for stackTrace: " + file.getPath());
                e.printStackTrace();
            }
            file.setReadable(true);
            fileArray[index] = file;
            index++;
        }
        return fileArray;
    }

    private void sendIntentToEmailApp(File[] filesToSend) {
        String body = "If this file was converted to 'plain text', save it to your computer with the .csv extension to easily open it with Excel\n";
        Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
        email.setData(Uri.parse("mailto:"));
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_SUBJECT, "EAF Toolkit DCP Project Summary");
        email.putExtra(Intent.EXTRA_TEXT, body);
        // TODO  Add supporting graphs?
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : filesToSend) {
            uris.add(Uri.fromFile(file));
        }
        email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(email, "Send Project in CSV File(s)"));
        }
    }
}

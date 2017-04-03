package com.bogueratcreations.eaftoolkit.DCP;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

public class Projects extends AppCompatActivity {

    // Email variables
    private static final String FILENAME = "EAF_Toolkit_CBRs.csv";
    //private Button btnSend;
    //private EditText editText;
    private FileWriter writer;
    private String emailBody = "No content passed...";

    // Database variables
    private Realm realm;
    RealmResults<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

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

    public void clickHandlerGraphProject(View view) {
        // Exporting data and attaching to an email.
        emailBody = "This is my email body that should also be the attachemnt";

        File fileToSend = createFileWithContent(emailBody);
        sendIntentToGmailApp(fileToSend);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }

    private void sendIntentToGmailApp(File fileToSend) {
        if(fileToSend != null){
            if ((!fileToSend.canRead()) || (!fileToSend.exists())) {
                Log.e("EAFToolkit", "File cant read or not exists (email get att): " + fileToSend.getPath());
            } else {
                Log.e("EAFToolkit", "File can read and exists (email get att): " + fileToSend.getPath());
            }
            fileToSend.setReadable(true);
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_SUBJECT, "Send Text File As Attachment Example");
            email.putExtra(Intent.EXTRA_TEXT, emailBody);
            email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileToSend.getPath()));
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email , "Send Text File"));
        }

    }

    private File createFileWithContent(String content) {

        if(TextUtils.isEmpty(content)){
            content = emailBody;
        }
        File file = null;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 55);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 55);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        try{
            Context thisContext = getApplicationContext();
            //file = new File(Environment.getExternalStorageDirectory(), FILENAME);
            file = new File(thisContext.getExternalCacheDir(), FILENAME);

            if ((!file.canRead()) || (!file.exists())) {
                Log.e("EAFToolkit", "File cant read or not exists 2: " + file.getPath());
            } else {
                Log.e("EAFToolkit", "File can read and exists 2: " + file.getPath());
            }
            writer = new FileWriter(file);
            writer.write("Here's my test content.");
            writer.close();
        } catch (IOException e) {
            Log.d("EAFToolkit", "Unable create temp file. Check logcat for stackTrace: " + file.getPath());
            e.printStackTrace();
        }
        return file;
    }
}

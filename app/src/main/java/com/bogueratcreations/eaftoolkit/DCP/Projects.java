package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Projects extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        realm = Realm.getDefaultInstance();

        RealmResults<Project> projects = realm.where(Project.class).findAll();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void clickHandlerGraphProject(View view) {

        Snackbar.make(view, "Will use this button to graph a project...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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

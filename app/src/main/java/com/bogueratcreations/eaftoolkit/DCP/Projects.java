package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Projects extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        realm = Realm.getDefaultInstance();

        RealmResults<Point> projects = realm.where(Point.class).findAll();
        final PointsListAdapter adapter = new PointsListAdapter(this, projects);

        ListView listView = (ListView) findViewById(R.id.projectListView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String projectName = adapter.getItem(i).getProjName();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Project.class).equalTo("projName", projectName).findAll().deleteAllFromRealm();
                    }
                });
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Will use this to create a new project...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(Projects.this, ProjectsAdd.class));

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void clickHandlerGraphProject(View view) {

        Snackbar.make(view, "Will use this button to graph a project...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}

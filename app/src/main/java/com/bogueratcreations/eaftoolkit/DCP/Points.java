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
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Points extends AppCompatActivity {

    long passedProjectID;

    private Realm realm;

    TextView tvProjName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        realm = Realm.getDefaultInstance();

        // Get the project name (ID) for use in finding associated points and for use in creating new points.
        passedProjectID = getIntent().getLongExtra("projId",-1);

        // TODO: If receiving -1, I wasn't passed a valid project...

        tvProjName = (TextView) findViewById(R.id.tvPointProject);
//        Log.e("EAFToolkit","Passed ProjectID - " + passedProjectID);
        Project passedProject = realm.where(Project.class)
                .equalTo("id",passedProjectID)
                .findFirst();
        tvProjName.setText(passedProject.getProjName());
//        Log.e("EAFToolkit","Passed Project Object -- " + passedProject);
        // Get list of points for this Project.
        RealmResults<Point> points = realm.where(Point.class)
                .equalTo("project.id",passedProjectID)
                .findAll();
        final PointsListAdapter adapter = new PointsListAdapter(this, points);

        ListView listView = (ListView) findViewById(R.id.listViewPoints);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long pointId = adapter.getItem(i).getId();
                Intent intent = new Intent(view.getContext(), PointsAdd.class);
                intent.putExtra("pointId", pointId);
                intent.putExtra("projectId", passedProjectID);
                startActivity(intent);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // Pass the selected Point and navigate to Reading.class
                final long pointId = adapter.getItemId(position);
                Intent intent = new Intent(view.getContext(), Readings.class);
                intent.putExtra("pointId", pointId);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PointsAdd.class);
//                intent.putExtra("pointId", pointId);
                intent.putExtra("projectId", passedProjectID);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

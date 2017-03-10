package com.bogueratcreations.eaftoolkit.DCP;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Readings extends AppCompatActivity {

    long passedPointID;

    private Realm realm;

    TextView tvPointInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        realm = Realm.getDefaultInstance();

        // Get the point name (ID) for use in finding associated points and for use in creating new points.
        passedPointID = getIntent().getLongExtra("pointName",-1);

        // TODO: If receiving -1, I wasn't passed a valid point...

        tvPointInfo = (TextView) findViewById(R.id.tvPointInfo);

        Point passedPoint = realm.where(Point.class)
                .equalTo("id", passedPointID)
                .findFirst();
        tvPointInfo.setText("Point: " + passedPoint.getPointNum());

        RealmResults<Reading> readings = realm.where(Reading.class)
                .equalTo("point.id", passedPointID)
                .findAll();
        final ReadingsListAdapter adapter = new ReadingsListAdapter(this, readings);

        ListView listView = (ListView) findViewById(R.id.listViewReadings);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long readingId = adapter.getItem(i).getId();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Reading.class)
                                .equalTo("id",readingId)
                                .findAll()
                                .deleteAllFromRealm();
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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Create a new Reading...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

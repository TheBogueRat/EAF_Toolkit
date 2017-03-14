package com.bogueratcreations.eaftoolkit.DCP;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Readings extends AppCompatActivity {

    long passedPointID;
    Point passedPoint;
    private Realm realm;

    TextView tvPointInfo;
    NumberPicker npHammer;
    NumberPicker npBlows;
    NumberPicker npDepth;

    long selectedReading = -1;

    Button btnAppend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        realm = Realm.getDefaultInstance();

        // Get the point name (ID) for use in finding associated points and for use in creating new points.
        passedPointID = getIntent().getLongExtra("pointId",-1);
        Log.d("Point id passed: ",String.valueOf(passedPointID));
        // TODO: If receiving -1, I wasn't passed a valid point...

        tvPointInfo = (TextView) findViewById(R.id.tvPointInfo);
        npHammer = (NumberPicker) findViewById(R.id.pickHammer);
        npBlows = (NumberPicker) findViewById(R.id.pickBlows);
        npDepth = (NumberPicker) findViewById(R.id.pickDepth);

        final String[] hammers = {"17.6", "10.1"};
        npHammer.setDisplayedValues(hammers);
        npHammer.setMinValue(0);
        npHammer.setMaxValue(hammers.length - 1);
        npHammer.setWrapSelectorWheel(true);
//        npHammer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
//                // Don't need this because I;ll grab the info when saved.
//            }
//        });
        final String[] blows = {"1","2","3","4","5","10","15","20","25","30","35","40","45","50"};
        npBlows.setDisplayedValues(blows);
        npBlows.setMinValue(0);
        npBlows.setMaxValue(blows.length - 1);
        npBlows.setWrapSelectorWheel(true);

        final String[] depths = {"25","30","35","40","45","50","55","60","65","70","75","80","85","90","95","100"};
        npDepth.setDisplayedValues(depths);
        npDepth.setMinValue(0);
        npDepth.setMaxValue(depths.length - 1);
        npDepth.setWrapSelectorWheel(true);

        passedPoint = realm.where(Point.class)
                .equalTo("id", passedPointID)
                .findFirst();
        tvPointInfo.setText("Point: " + passedPoint.getPointNum());

        btnAppend = (Button) findViewById(R.id.btnReading);
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Reading newReading = new Reading();
                newReading.setId(PrimaryKeyFactory.getInstance().nextKey(Reading.class));
                newReading.setReadingNum(passedPoint.getReadings().size()); // size is next number in zero-based array.
                newReading.setHammer(npHammer.getValue());
                newReading.setBlows(npBlows.getValue());
                newReading.setDepth(npDepth.getValue());
                newReading.setPoint(passedPoint);
                newReading.setCbr(Math.random()*100);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(newReading);
                passedPoint.getReadings().add(newReading);
                realm.commitTransaction();
            }
        });

        final RealmResults<Reading> readings = realm.where(Reading.class)
                .equalTo("point.id", passedPointID)
                .findAll()
                .sort("readingNum");

        final ReadingsListAdapter adapter = new ReadingsListAdapter(this, readings);
        ListView listView = (ListView) findViewById(R.id.listViewReadings);
        LayoutInflater inflater = getLayoutInflater();
//        // Was adding a header but it scrolls up with the layout.
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listview_readings_header,listView,false);
//        listView.addHeaderView(header, null, false);
        listView.setAdapter(adapter);

//        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setBackgroundColor(Color.RED);  // causes a quick flash of red to acknowledge the delete.
                final long readingId = adapter.getItem(position).getId(); // minus header row!!!!
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Reading.class)
                                .equalTo("id",readingId)
                                .findAll()
                                .deleteAllFromRealm();
                        int i = 0;
                        RealmResults<Reading> readingResults = realm.where(Reading.class).equalTo("point.id",passedPointID).findAll().sort("readingNum");
                        for (Reading r : readingResults) {
                            r.setReadingNum(i);
                            i++;
                        }
                    }
                });
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                view.setSelected(true);
                adapterView.setSelected(true);
                selectedReading = position;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create a new Reading...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    void clickHandlerReading(View view) {
//        final Reading newReading = new Reading();
//        newReading.setId(PrimaryKeyFactory.getInstance().nextKey(Reading.class));
//        newReading.setReadingNum(passedPoint.getReadings().size()); // size is next number in zero-based array.
//        newReading.setHammer(npHammer.getValue());
//        newReading.setBlows(npBlows.getValue());
//        newReading.setDepth(npDepth.getValue());
//        newReading.setPoint(passedPoint);
//        newReading.setCbr(Math.random()*100);
//
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(newReading);
//        passedPoint.getReadings().add(newReading);
//        realm.commitTransaction();
//
        // Async didn't work since we are updating an existing object.
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) { // Need be done on same thread NOTES ....
//                realm.copyToRealmOrUpdate(newReading);
//                passedPoint.getReadings().add(newReading);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

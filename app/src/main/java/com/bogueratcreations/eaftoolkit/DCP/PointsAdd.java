package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;
import com.bogueratcreations.eaftoolkit.common.saveDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class PointsAdd extends AppCompatActivity {

    EditText pointName;
    static EditText pointDate;
    TextView tvSoilType;

    long passedProjectID;
    Project passedProject;
    long passedPointID; // Only if editing a Point
    Point passedPoint;

    private Realm realm;

    static Boolean hasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        pointName = (EditText) findViewById(R.id.etPointName);
        pointDate = (EditText) findViewById(R.id.etPointDate);
        tvSoilType = (TextView) findViewById(R.id.tvPointSoilType);

        passedProjectID = getIntent().getLongExtra("projectId", -1);
        if (passedProjectID > -1) {
            passedProject = realm.where(Project.class)
                    .equalTo("id", passedProjectID)
                    .findFirst();
        }
        passedPointID = getIntent().getLongExtra("pointId", -1);
        if (passedPointID == -1) {
            String currentDateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
            pointDate.setText(currentDateTimeString);
        } else {
            passedPoint = realm.where(Point.class)
                    .equalTo("id", passedPointID)
                    .findFirst();
            pointName.setText(passedPoint.getPointNum());
            String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(passedPoint.getDate());
            pointDate.setText(dateTimeString);
            switch(passedPoint.getProject().getSoilType()) {
                case 0:
                    tvSoilType.setText("Low Plasticity Clay");
                    break;
                case 1:
                    tvSoilType.setText("High Plasticity Clay");
                    break;
                case 2:
                    tvSoilType.setText("All Other Soils");
                    break;
            }
        }
        pointDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPointSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the changes
                saveData();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveData() {
        // Retrieve appropriate date
        DateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
        Date dateObject = null;
        String projDate = pointDate.getText().toString();
        try {
            dateObject = formatter.parse(projDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date finalDate = dateObject;
        // Save data
        if (passedPointID == -1) {
            // Saving a new point, adding to passedProject
            final Point point = new Point();
            point.setId(PrimaryKeyFactory.getInstance().nextKey(Point.class));
            point.setPointNum(pointName.getText().toString());
            point.setDate(finalDate);
            point.setSoilType(passedPoint.getProject().getSoilType());
            point.setCbr(0.0);
            point.setProject(passedProject);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(point);
                }
            });
        } else {
            // Updating and saving the Point passed in
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // update passedPoint (update changes into Project?)
                    passedPoint.setPointNum(pointName.getText().toString());
                    passedPoint.setDate(finalDate);
                    realm.copyToRealmOrUpdate(passedPoint);
                }
            });
        }
    }

    public void saveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Data?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Save Data
                saveData();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just quit
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            // TODO: Check if user want's to save the changed information...
            saveDialog();
        }
        super.onBackPressed();
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new ProjectsAdd.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            pointDate.setText(String.format("%02d", (month + 1)) + "/" + String.format("%02d", day) + "/" + year);
            hasChanged = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}

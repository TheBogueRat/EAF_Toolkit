package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Points extends AppCompatActivity {

    long passedProjectID;
    Project passedProject;
    int passedProjectSoilType;
    long selectedPointID;
    Point selectedPoint;

    boolean editMode = false; // Used to control the resulting FAB action
    boolean newPoint = false; // Edit mode just means we are editing or adding a point, this indicates editing a newPoint
    static Boolean hasChanged = false;

    private Realm realm;

    TextView tvProjName;
    LinearLayout defaultView;
    LinearLayout editView;

    EditText pointName;
    static EditText pointDate;
    TextView tvSoilType;
    Button btnSaveEdit;

    // TODO: Capture back button in edit mode and ask about saving....
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        realm = Realm.getDefaultInstance();

        // Get the project name (ID) for use in finding associated points and for use in creating new points.
        passedProjectID = getIntent().getLongExtra("projId",-1);
        Log.d("passedProjID in Pts: ", String.valueOf(passedProjectID));
        // TODO: If receiving -1, I wasn't passed a valid project...
        passedProject = realm.where(Project.class)
                .equalTo("id",passedProjectID)
                .findFirst();
        passedProjectSoilType = passedProject.getSoilType();
        Log.d("Passed Project Info: ", passedProject.toString());

        tvProjName = (TextView) findViewById(R.id.tvPointProject);
        defaultView = (LinearLayout) findViewById(R.id.viewPoints);
        editView = (LinearLayout) findViewById(R.id.viewAddPoint);
            // editView items
            pointName = (EditText) findViewById(R.id.etPointName);
            pointDate = (EditText) findViewById(R.id.etPointDate);
            tvSoilType = (TextView) findViewById(R.id.tvPointSoilType);
        tvProjName.setText(passedProject.getProjName());
        switch(passedProjectSoilType) {
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
        pointDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
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
                selectedPointID = adapter.getItem(i).getId(); // TODO: verifiy this is getting the Point id not just an arbitrary id from the list.
                defaultView.setVisibility(View.GONE);
                editView.setVisibility(View.VISIBLE);
                btnSaveEdit.setText("Graph");
                editMode = true;
                // TODO: Change FAB to save button

                // Populate the edit form
                selectedPoint = realm.where(Point.class)
                        .equalTo("id", selectedPointID)
                        .findFirst();
                pointName.setText(selectedPoint.getPointNum());

                String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(selectedPoint.getDate());
                pointDate.setText(dateTimeString);
                // TODO: The soil type is based on the project, displaying this to remind user.
                switch(passedProjectSoilType) {
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
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // Pass the selected Point and navigate to Reading.class
                final long pointId = adapter.getItem(position).getId();
                Intent intent = new Intent(view.getContext(), Readings.class);
                intent.putExtra("pointId", pointId);
                startActivityForResult(intent, 1);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSaveEdit = (Button) findViewById(R.id.btnNew_Save);
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    defaultView.setVisibility(View.VISIBLE);
                    editView.setVisibility(View.GONE);
                    editMode = false;
                    // Change FAB to New button
                    // Was editing a Point so need to save the changes
                    if(newPoint) {
                        newPoint = false;
                        savePoint();
                    } else {
                        updatePoint();
                    }
                    btnSaveEdit.setText("New");
                } else {
                    defaultView.setVisibility(View.GONE);
                    editView.setVisibility(View.VISIBLE);
                    editMode = true;
                    newPoint = true;
                    // Change FAB to save button
                    btnSaveEdit.setText("Save");
                }
                String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
                pointDate.setText(dateTimeString);
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPoints);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    fab.setImageResource(R.drawable.icon_save);
                    // Display edit view
                } else {
                    fab.setImageResource(R.drawable.);
                }
//                Intent intent = new Intent(view.getContext(), PointsAdd.class);
////                intent.putExtra("pointId", pointId);
//                intent.putExtra("projectId", passedProjectID);
//                startActivity(intent);
            }
        });
        // Not using acctionBar back button any longer
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void savePoint() {

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
        // Saving a new point, adding to passedProject
        final Point point = new Point();
        point.setId(PrimaryKeyFactory.getInstance().nextKey(Point.class));
        point.setPointNum(pointName.getText().toString());
        point.setDate(finalDate);
        point.setSoilType(passedProjectSoilType); // TODO: Passed Project is null???
        point.setCbr(0.0);
        point.setProject(passedProject);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(point);
                passedProject.getPoints().add(point);
//                realm.copyToRealmOrUpdate(passedProject); // TODO: do I need to save this here or is it implicit in the change?
            }
        });
    }

    private void updatePoint() {
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
        // Updating and saving the Point passed in
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // update passedPoint (update changes into Project?)
                selectedPoint.setPointNum(pointName.getText().toString());
                selectedPoint.setDate(finalDate);
                realm.copyToRealmOrUpdate(selectedPoint);
            }
        });
    }

    public void saveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Data?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Save Data
                if(editMode) {
                    updatePoint();
                } else {
                    savePoint();
                }
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
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new Points.pointDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class pointDatePickerFragment extends DialogFragment implements
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                passedProjectID = data.getLongExtra("passedProjectID", -1);  // TODO: Don't think I need this here since I use the same key to receive this from Projects.class
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        // Capture back pressed if in edit mode and save the data as requested.
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }
}

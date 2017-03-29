package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Points extends AppCompatActivity {

    long passedProjectID;
    Project passedProject;
    int passedProjectSoilType;
    String projectSoilType;
    long selectedPointID;
    Point selectedPoint;

    boolean editMode = false; // Used to control the resulting FAB action
    boolean newPoint = false; // Edit mode just means we are editing or adding a point, this indicates editing a newPoint
    static Boolean hasChanged = false; // Whether changes have been made to the Point.

    private Realm realm;

    TextView tvProjName;
    LinearLayout defaultView;
    LinearLayout editView;

    EditText pointName;
    static EditText pointDate;
    TextView tvSoilType;
    Button btnSaveEdit;  // This is now the Graph button...

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
                projectSoilType = "Low Plasticity Clay";
                break;
            case 1:
                projectSoilType = "High Plasticity Clay";
                break;
            case 2:
                projectSoilType = "All Other Soils";
                break;
        }
        tvSoilType.setText(projectSoilType);
        pointDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Force close the soft keyboard if open.
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(pointName.getWindowToken(), 0);
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
                editMode = true;
                hasChanged = false;
                toggleButton();
                // TODO: Change FAB to save button
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPoints);
                fab.setImageResource(R.drawable.ic_action_save);

                // Populate the edit form
                selectedPoint = realm.where(Point.class)
                        .equalTo("id", selectedPointID)
                        .findFirst();
                pointName.setText(selectedPoint.getPointNum());

                String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(selectedPoint.getDate());
                pointDate.setText(dateTimeString);
                // TODO: The soil type is based on the project, displaying this to remind user.
                tvSoilType.setText(projectSoilType);
                // Returns true to prevent onItemClickListener from firing
                return true;
            }
        });
//
        pointName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasChanged = true;
            }
            @Override
            public void afterTextChanged(Editable s) {
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
                    // Button only appears in edit mode if editing an existing Point, not if adding a new one.
                    realm.beginTransaction();
                        // Removed reference to that point from the passed project.
                        passedProject.getPoints().remove(selectedPoint);
                        // Remove all readings for this point and remove point
                        selectedPoint.cascadeDeletePoint();
                    realm.commitTransaction();
                    revertView();
                } else {
                    // Pass the selected Point and navigate to Reading.class
                    Intent intent = new Intent(view.getContext(), PointsChart.class);
                    intent.putExtra("projId", passedProjectID);
                    startActivityForResult(intent, 1);
                }
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPoints);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMode) {
                    // Force close the soft keyboard if open.
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(pointName.getWindowToken(), 0);
                    // Change FAB to New button, toggle views, and save info
                    fab.setImageResource(R.drawable.ic_action_add);
                    defaultView.setVisibility(View.VISIBLE);
                    editView.setVisibility(View.GONE);
                    editMode = false;
                    // Was editing a Point so need to save the changes
                    if(newPoint) {
                        newPoint = false;
                        savePoint();
                    } else {
                        updatePoint();
                    }
                    hasChanged = false;
                } else {
                    // Creating a new point...Change FAB to save button and toggle views.
                    fab.setImageResource(R.drawable.ic_action_save);
                    defaultView.setVisibility(View.GONE);
                    editView.setVisibility(View.VISIBLE);
                    editMode = true;
                    newPoint = true;
                    String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
                    pointDate.setText(dateTimeString);
                    pointName.setText("Point ");
                    hasChanged = false;
                }
                toggleButton();
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
                if(newPoint) {
                    savePoint();
                } else {
                    updatePoint();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just return to Point view.
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

    public void revertView() {
        // Change FAB to New button and toggle views
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPoints);
        fab.setImageResource(R.drawable.ic_action_add);
        defaultView.setVisibility(View.VISIBLE);
        editView.setVisibility(View.GONE);
        hasChanged = false;
        editMode = false;
        newPoint = false;
        // After updating variables, change the button view.
        toggleButton();
    }

    public void toggleButton() {
        if ((editMode) && (!newPoint)) {
            btnSaveEdit.setText("Delete");
            btnSaveEdit.setTextColor(Color.RED);
            btnSaveEdit.setBackgroundColor(Color.RED);
            btnSaveEdit.setEnabled(true);
        } else if (editMode) {
            btnSaveEdit.setText("");
            btnSaveEdit.setBackgroundColor(getResources().getColor(R.color.BRCgreen));
            btnSaveEdit.setEnabled(false);
        } else {
            btnSaveEdit.setText("Graph");
            btnSaveEdit.setTextColor(Color.WHITE);
            btnSaveEdit.setBackgroundColor(getResources().getColor(R.color.BRCgreen));
            btnSaveEdit.setEnabled(true);
        }
    }
    @Override
    public void onBackPressed() {
        // Capture back pressed if in edit mode and save the data as requested.
        if ((editMode) && (hasChanged)) {
            saveDialog();
            revertView();
        } else if (editMode) {
            revertView();
        } else {
            // Go ahead and move back to the Projects page.
            finish();
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
}

package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

public class ProjectsAdd extends AppCompatActivity {

    String DEBUG_STRING = "EAFToolkit";

    static boolean dataChanged = false;

    EditText projectName;
    EditText projectLoc;
    EditText projectInfo;
    TextView tvLatitude;
    TextView tvLongitude;
    static EditText projectDate;
    SegmentedGroup projectSoilType;
    RadioButton btnLowClay;
    RadioButton btnHighClay;
    RadioButton btnAllSoils;
    Button btnDeleteProject;
    Button btnGPS;
    EditText projectSoilInfo;

    DatePickerDialog datePickerDialog;
    Calendar startingDate = Calendar.getInstance();

    long passedProjectId;  // If -1 then create a new one, else edit.
    Project passedProject;  // Used to hold the passed project as necessary.
    long passedSoilType;  // Need to update all Points @ readings if soil type changes.
    private Realm realm;

    double projectLatitude = -1;
    double projectLongitude = -1;
    double oldLat;
    double oldLng;

    // To detect Changes in date
    static int projectDay;
    static int projectMo;
    static int projectYear;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setTitle("Add/Edit Project");
        realm = Realm.getDefaultInstance();

        projectName = (EditText) findViewById(R.id.etProjName);
        projectName.addTextChangedListener(textWatcher);
        projectLoc = (EditText) findViewById(R.id.etProjLoc);
        projectLoc.addTextChangedListener(textWatcher);
        tvLatitude = (TextView) findViewById(R.id.tvLattitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        projectInfo = (EditText) findViewById(R.id.etProjInfo);
        projectInfo.addTextChangedListener(textWatcher);
        projectDate = (EditText) findViewById(R.id.etProjDate);
        projectSoilType = (SegmentedGroup) findViewById(R.id.segmentSoil);
        projectSoilType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                dataChanged = true;
            }
        });

        projectSoilInfo = (EditText) findViewById(R.id.etSoilInfo);
        projectSoilInfo.addTextChangedListener(textWatcher);
        btnLowClay = (RadioButton) findViewById(R.id.btnLowClay);
        btnHighClay = (RadioButton) findViewById(R.id.btnHighClay);
        btnAllSoils = (RadioButton) findViewById(R.id.btnAllSoils);
        btnDeleteProject = (Button) findViewById(R.id.btnDeleteProject);
        btnGPS = (Button) findViewById(R.id.btnGPS);
        projectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });


        // Check for add/edit
        passedProjectId = getIntent().getLongExtra("projectId", -1);
        if (passedProjectId == -1) {
            // Adding a new Project
            String currentDateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
            // textView is the TextView view that should display it
            projectDate.setText(currentDateTimeString);
            startingDate.setTime(new Date());
            projectDay = startingDate.get(Calendar.DAY_OF_MONTH);
            projectMo = startingDate.get(Calendar.MONTH);
            projectYear = startingDate.get(Calendar.YEAR);
            // Hide DELETE button
            btnDeleteProject.setVisibility(View.GONE);
        } else {
            // Get the passed Project object and populate the fields.
            passedProject = realm.where(Project.class)
                    .equalTo("id",passedProjectId)
                    .findFirst();
            projectName.setText(passedProject.getProjName());
            projectLoc.setText(passedProject.getProjLoc());
            projectInfo.setText(passedProject.getProjInfo());
            String dateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(passedProject.getDateCreated());
            projectDate.setText(dateTimeString);
            startingDate.setTime(passedProject.getDateCreated());
            projectDay = startingDate.get(Calendar.DAY_OF_MONTH);
            projectMo = startingDate.get(Calendar.MONTH);
            projectYear = startingDate.get(Calendar.YEAR);
            switch(passedProject.getSoilType()) {
                case 0:
                    btnLowClay.setSelected(true);
                    btnLowClay.setChecked(true);
                    break;
                case 1:
                    btnHighClay.setSelected(true);
                    btnHighClay.setChecked(true);
                    break;
                case 2:
                    btnAllSoils.setSelected(true);
                    btnAllSoils.setChecked(true);
                    break;
            }
            // Disable all radio buttons after setting the selected one...
            btnLowClay.setEnabled(false);
            btnHighClay.setEnabled(false);
            btnAllSoils.setEnabled(false);
            projectSoilInfo.setText(passedProject.getSoilInfo());
            projectLatitude = passedProject.getLatitude();
            projectLongitude = passedProject.getLongitude();
            setLatLongText();
        }

        btnDeleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveProject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Saving no matter what, that's what the button is there for!
                saveData();
                // close activity and return to previous
                finish();
            }
        });

        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProjectsMapActivity.class);
                intent.putExtra("latitude", projectLatitude);
                intent.putExtra("longitude", projectLongitude);
                intent.putExtra("projectId", passedProjectId);
                oldLat = projectLatitude;
                oldLng = projectLongitude;
                Log.d(DEBUG_STRING, "Lat: " + projectLatitude + "  Lng: " + projectLongitude);
                startActivityForResult(intent, 23);
            }
        });
        // Reset flag after preloading form fields.
        dataChanged = false;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            dataChanged = true;
        }
    };

    private void saveData() {
        // Retrieve appropriate date
        DateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
        Date dateObject = null;
        String projDate = projectDate.getText().toString();
        try {
            dateObject = formatter.parse(projDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date finalDate = dateObject;
        // Save current Project or create a new one
        if (passedProjectId == -1) {
            // Create new Project
            final Project project = new Project();
            // Assign a new ID, else use the existing one
            project.setId(PrimaryKeyFactory.getInstance().nextKey(Project.class));
            // Set values for Project
            project.setProjName(projectName.getText().toString());
            project.setProjLoc(projectLoc.getText().toString());
            project.setProjInfo(projectInfo.getText().toString());
            project.setDateCreated(finalDate);
            project.setSoilType(projectSoilType.indexOfChild(findViewById(projectSoilType.getCheckedRadioButtonId())));
            project.setSoilInfo(projectSoilInfo.getText().toString());
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(project);
                }
            });
        } else {
            // if soil type changes, update all other entries.
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Update passedProject
                    passedProject.setProjName(projectName.getText().toString());
                    passedProject.setProjLoc(projectLoc.getText().toString());
                    passedProject.setProjInfo(projectInfo.getText().toString());
                    passedProject.setLatitude(projectLatitude);
                    passedProject.setLongitude(projectLongitude);
                    passedProject.setDateCreated(finalDate);
                    passedProject.setSoilType(projectSoilType.indexOfChild(findViewById(projectSoilType.getCheckedRadioButtonId())));
                    passedProject.setSoilInfo(projectSoilInfo.getText().toString());
                    // Record changes, if any.
                    realm.copyToRealmOrUpdate(passedProject);
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23) {
            if (resultCode == RESULT_OK) {
                // retrieve location data from Projects Map Activity
                projectLatitude = data.getDoubleExtra("lattitude", -1.0);
                projectLongitude = data.getDoubleExtra("longitude", -1.0);
                setLatLongText();
                if ((oldLat != projectLatitude) || (oldLng != projectLongitude)) {
                    dataChanged = true;
                    Log.d(DEBUG_STRING, "Detected Location Change and toggled dataChanged variable to " + dataChanged);
                }
//                passedProjectID = data.getLongExtra("passedProjectID", -1);  // TODO: Don't think I need this here since I use the same key to receive this from Projects.class
            }
        }
    }

    private void setLatLongText() {
        // Set displayed latitude and longitude
        if (projectLatitude != -1) {
            tvLatitude.setText("Lat:  " + projectLatitude);
        } else {
            tvLatitude.setText("Lat:  n/a");
        }
        if (projectLongitude != -1) {
            tvLongitude.setText("Long: " + projectLongitude);
        } else {
            tvLongitude.setText("Long: n/a");
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @NonNull
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
            if ((year != projectDay) || (month != projectMo) || (day != projectDay)) {
                projectDate.setText(String.format("%02d", (month + 1)) + "/" + String.format("%02d", day) + "/" + year);
                // Update the project date so we can detect future changes
                projectDay = day;
                projectYear = year;
                projectMo = month;
                dataChanged = true;
            }
        }
    }

    public void saveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Changes?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Save Data
                saveData();
                dataChanged = false;
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just return to Projects view.
                dataChanged = false;
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this point?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete this project, its points and readings.  Return to Projects
                realm.beginTransaction();
                passedProject.cascadeDeleteProject();
                realm.commitTransaction();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just return to Projects view.
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Log.d(DEBUG_STRING, "onBackPressed dataChanged: " + dataChanged);
        if (dataChanged) {
            saveDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

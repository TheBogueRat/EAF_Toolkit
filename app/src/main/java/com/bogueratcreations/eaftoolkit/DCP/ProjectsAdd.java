package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ProjectsAdd extends AppCompatActivity {

    EditText projectName;
    EditText projectLoc;
    EditText projectInfo;
    static EditText projectDate;
    SegmentedGroup projectSoilType;
    RadioButton btnLowClay;
    RadioButton btnHighClay;
    RadioButton btnAllSoils;
    Button btnDeleteProject;
    EditText projectSoilInfo;

    DatePickerDialog datePickerDialog;

    long passedProjectId;  // If -1 then create a new one, else edit.
    Project passedProject;  // Used to hold the passed project as necessary.
    long passedSoilType;  // Need to update all Points @ readings if soil type changes.
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_add);

        realm = Realm.getDefaultInstance();

        projectName = (EditText) findViewById(R.id.etProjName);
        projectLoc = (EditText) findViewById(R.id.etProjLoc);
        projectInfo = (EditText) findViewById(R.id.etProjInfo);
        projectDate = (EditText) findViewById(R.id.etProjDate);
        projectSoilType = (SegmentedGroup) findViewById(R.id.segmentSoil);
        projectSoilInfo = (EditText) findViewById(R.id.etSoilInfo);
        btnLowClay = (RadioButton) findViewById(R.id.btnLowClay);
        btnHighClay = (RadioButton) findViewById(R.id.btnHighClay);
        btnAllSoils = (RadioButton) findViewById(R.id.btnAllSoils);
        btnDeleteProject = (Button) findViewById(R.id.btnDeleteProject);

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
            projectSoilInfo.setText(passedProject.getSoilInfo());
        }

        btnDeleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete this project, its points and readings.  Return to Projects
                realm.beginTransaction();
                passedProject.cascadeDeleteProject();
                realm.commitTransaction();
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveProject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            passedProject.setDateCreated(finalDate);
                            passedProject.setSoilType(projectSoilType.indexOfChild(findViewById(projectSoilType.getCheckedRadioButtonId())));
                            passedProject.setSoilInfo(projectSoilInfo.getText().toString());
                            // Record changes, if any.
                            realm.copyToRealmOrUpdate(passedProject);
                        }
                    });
                }
                // close activity and return to previous
                finish();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
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
            projectDate.setText(String.format("%02d", (month + 1)) + "/" + String.format("%02d", day) + "/" + year);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

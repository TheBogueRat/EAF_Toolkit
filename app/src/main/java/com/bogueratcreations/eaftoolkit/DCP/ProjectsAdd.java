package com.bogueratcreations.eaftoolkit.DCP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.Realm;

public class ProjectsAdd extends AppCompatActivity {

    static EditText projectName;
    static EditText projectLoc;
    static EditText projectInfo;
    static EditText projectDate;
    static SegmentedGroup projectSoilType;
    static EditText projectSoilInfo;

    static Button projectSave;

    DatePickerDialog datePickerDialog;

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

        projectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        String currentDateTimeString = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        // textView is the TextView view that should display it
        projectDate.setText(currentDateTimeString);

    }

    public void clickHandler(View view) {
        if (view.getId()== R.id.btnSaveProject) {
            // Save the current record and return to the master list
            Log.e("EAF_Toolkit","Saving...");
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DateFormat formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
                    Date dateObject = null;
                    String projDate = projectDate.getText().toString();
                    try {
                        dateObject = formatter.parse(projDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Project project = realm.createObject(Project.class);
                    project.setProjName(projectName.getText().toString());
                    project.setProjLoc(projectLoc.getText().toString());
                    project.setProjInfo(projectInfo.getText().toString());
                    project.setDateCreated(dateObject);
                    project.setSoilType(projectSoilType.indexOfChild(findViewById(projectSoilType.getCheckedRadioButtonId())));
                    project.setSoilInfo(projectSoilInfo.getText().toString());
                }
            });
            // TODO: Add snackbars for success or failure...
//                Snackbar.make(view, "Will use this to create a new project...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            // close activity and return to previous
            finish();
        }
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
            projectDate.setText(String.format("%02d", day) + "/" + String.format("%02d", (month + 1)) + "/" + year);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

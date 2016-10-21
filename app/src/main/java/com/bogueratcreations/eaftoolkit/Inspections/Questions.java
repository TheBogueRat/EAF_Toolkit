package com.bogueratcreations.eaftoolkit.Inspections;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bogueratcreations.eaftoolkit.Prefs;
import com.bogueratcreations.eaftoolkit.R;

import java.io.IOException;
import java.util.ArrayList;

public class Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.BRCgreen)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Exporting Questions via Email if possible on your device.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    exportQuestions();
                } catch (IOException e) {
                    Log.d("EAFToolkit", "Caught IOException: " + e);
                }
            }
        });
    }

    // TODO: Working to send data to the user via Email that can be opened as CSV or in Custom MS Excel Document
    // TODO: Because DB has combined fields, will have to send everything and sort it out on the other end.
    private void exportQuestions() throws IOException {

        //Intent intent = getIntent();
        String progName = Prefs.readString(getApplicationContext(), "PROGRAM", "");
        String tableName = Prefs.readString(getApplicationContext(), "TABLE", "");
        // TODO - Get table and prog from sharedPreferences

        InspectDBHandler handler = new InspectDBHandler(this);
        handler.setTableName(tableName);
        // Get SQL data via Question class method getQuestions(program) which returns ArrayList of Question.class objects
        ArrayList<Question> questionList = handler.getQuestions(progName);

        String bodyText = "";
        for (int loop = 0; loop <= questionList.size() - 1; loop++) {
            bodyText += questionList.get(loop).getQuestion() + "\r\n";
            bodyText += questionList.get(loop).getReference() + "\r\n";
            bodyText += "\r\n";
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, progName);
        sendIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
        sendIntent.setType("text/html");
        try {
            startActivity(sendIntent);
        } catch (Exception e) {}
    }
}

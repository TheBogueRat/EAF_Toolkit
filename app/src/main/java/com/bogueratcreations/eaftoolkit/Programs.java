package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Programs extends AppCompatActivity {

    //public final static String EXTRA_MESSAGE = "BRC.MESSAGE_PROGRAM";
    //public final static String EXTRA_MESSAGE_TABLE = "BRC.MESSAGE_TABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        //Intent intent = getIntent();
        // Save variable to the class to make it available to the fragment (not sure this is correct)
        //incomingMsg = intent.getStringExtra(Inspect.EXTRA_MESSAGE);

        setContentView(R.layout.activity_programs);

    }
}

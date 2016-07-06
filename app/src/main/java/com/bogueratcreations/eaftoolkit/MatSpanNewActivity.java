package com.bogueratcreations.eaftoolkit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MatSpanNewActivity extends AppCompatActivity {
    MatSpanDbAdapter adapter;
    MatSpanDbHelper helper;
    EditText etName;
    Button submitBtn, resetBtn;
    NumberPicker npWid, npLen, npSpans;
    MatSpanModel newSpan = new MatSpanModel();
    TextView tvSummary;
    SegmentedGroup segLay, segStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_span_new);
        etName = (EditText) findViewById(R.id.et_name);
        submitBtn = (Button) findViewById(R.id.btn_update);
        resetBtn = (Button) findViewById(R.id.btn_delete);
        npWid = (NumberPicker) findViewById(R.id.npWid);
        npLen = (NumberPicker) findViewById(R.id.npLen);
        npSpans = (NumberPicker)findViewById(R.id.npSpans);
        tvSummary = (TextView)findViewById(R.id.tvSummary);
        segLay = (SegmentedGroup)findViewById(R.id.segmentLay);
        segStart = (SegmentedGroup)findViewById(R.id.segmentStart);


        adapter = new MatSpanDbAdapter(this);
        // Set up number picker for Width
        npWid.setMinValue(12);
        npWid.setMaxValue(1500);
        npWid.setValue(96);
        npWid.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if (newVal<12) {
                    newVal = 12;
                } else if (Math.abs(newVal-oldVal)>6) {
                    // Assume the user typed in a new value more than a 6 step, Ensure it is divisible by 6
                    newVal = newVal/6 *6;
                    // Update the view
                } else {
                    newVal = (newVal < oldVal) ? oldVal - 6 : oldVal + 6;
                }
                npWid.setValue(newVal);
                newSpan.setWid(newVal);
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
            }
        });
        // Setup number picker for Length
        npLen.setMinValue(2);
        npLen.setMaxValue(20000);
        npLen.setValue(96);
        npLen.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if (newVal<2) {
                    newVal = 2;
                } else if (Math.abs(newVal-oldVal)>6) {
                    // Assume the user typed in a new value more than a 6 step, Ensure it is divisible by 6
                    newVal = newVal/2 *2;
                    // Update the view
                } else {
                    newVal = (newVal < oldVal) ? oldVal - 2 : oldVal + 2;
                }
                npLen.setValue(newVal);
                newSpan.setLen(newVal);
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
            }
        });
        // Set up number picker for the number of spans
        npSpans.setMinValue(1);
        npSpans.setMaxValue(99);
        npSpans.setValue(1);
        npSpans.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                newSpan.setQty(newVal);
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
            }
        });

        // Set click actions for reset button - ?????
        resetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                etName.setText("");
            }
        });

        //  Setup toolbar item - is FAB part of this object?
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameVal = etName.getText().toString();
                int widVal = npWid.getValue();
                int lenVal = npLen.getValue();
                int spansVal = npSpans.getValue();
                int layVal = segLay.indexOfChild(findViewById(segLay.getCheckedRadioButtonId()));
                int startVal = segStart.indexOfChild(findViewById(segStart.getCheckedRadioButtonId()));
                // Create database entry
                long val = adapter.insertMatSpan(nameVal, widVal, lenVal, spansVal, layVal, startVal, 0);
                //Toast.makeText(getApplicationContext(), Long.toString(val), Toast.LENGTH_SHORT).show();
                Snackbar.make(view, Long.toString(val), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

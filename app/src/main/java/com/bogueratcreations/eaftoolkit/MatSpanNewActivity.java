package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MatSpanNewActivity extends AppCompatActivity {
    MatSpanDbAdapter adapter;
    MatSpanDbHelper helper;
    EditTextBackEvent etName;
    NumberPicker npWid, npLen, npSpans;
    MatSpanModel newSpan = new MatSpanModel();
    TextView tvSummary;
    SegmentedGroup segLay, segStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_span_new);
        etName = (EditTextBackEvent) findViewById(R.id.et_name);
        npWid = (NumberPicker) findViewById(R.id.npWid);
        npLen = (NumberPicker) findViewById(R.id.npLen);
        npSpans = (NumberPicker)findViewById(R.id.npSpans);
        tvSummary = (TextView)findViewById(R.id.tvSummary);
        segLay = (SegmentedGroup)findViewById(R.id.segmentLay);
        segStart = (SegmentedGroup)findViewById(R.id.segmentStart);

        adapter = new MatSpanDbAdapter(this);
        etName.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextBackEvent ctrl, String text) {
                newSpan.setName(etName.getText().toString());
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // if lost focus, update name entry.
                    EditText editText = (EditText) view;
                    newSpan.setName(editText.getText().toString());
                    newSpan.calcMat();
                    tvSummary.setText(newSpan.summarize());
                }
            }
        });
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm= (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
                    etName.clearFocus();
                    return true;
                }
                return false;
            }
        });
        // Set up number picker for Width
        npWid.setMinValue(0);
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
        EditText etNpWid = null;
        int childCountWid = npWid.getChildCount();
        for (int i = 0; i < childCountWid; i++) {
            View childView = npWid.getChildAt(i);
            if (childView instanceof EditText) {
                etNpWid = (EditText) childView;
            }
        }
        if (etNpWid != null) {
            etNpWid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // By clearing focus, the numberpicker refreshes it's value triggering the summary update
                        npWid.clearFocus();
                        return true;
                    }
                    return false;
                }
            });
        }
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
        EditText etNpLen = null;
        int childCountLen = npLen.getChildCount();
        for (int i = 0; i < childCountLen; i++) {
            View childView = npLen.getChildAt(i);
            if (childView instanceof EditText) {
                etNpLen = (EditText) childView;
            }
        }
        if (etNpLen != null) {
            etNpLen.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // By clearing focus, the numberpicker refreshes it's value triggering the summary update
                        npLen.clearFocus();
                        return true;
                    }
                    return false;
                }
            });
        }
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
        EditText etNpSpans = null;
        int childCountSpans = npWid.getChildCount();
        for (int i = 0; i < childCountSpans; i++) {
            View childView = npWid.getChildAt(i);
            if (childView instanceof EditText) {
                etNpSpans = (EditText) childView;
            }
        }
        if (etNpSpans != null) {
            etNpSpans.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // By clearing focus, the numberpicker refreshes it's value triggering the summary update
                        npWid.clearFocus();
                        return true;
                    }
                    return false;
                }
            });
        }
        segLay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int layVal = segLay.indexOfChild(findViewById(segLay.getCheckedRadioButtonId()));
                newSpan.setLay(layVal);
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
            }
        });
        segStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int startVal = segStart.indexOfChild(findViewById(segStart.getCheckedRadioButtonId()));
                newSpan.setStart(startVal);
                newSpan.calcMat();
                tvSummary.setText(newSpan.summarize());
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
                adapter.insertMatSpan(nameVal, widVal, lenVal, spansVal, layVal, startVal, 0);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

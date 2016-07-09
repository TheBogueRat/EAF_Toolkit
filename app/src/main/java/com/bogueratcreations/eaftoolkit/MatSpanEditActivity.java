package com.bogueratcreations.eaftoolkit;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

//import com.bogueratcreations.eafToolkit.R;

import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MatSpanEditActivity extends AppCompatActivity {

    MatSpanDbAdapter adapter;
    MatSpanDbHelper helper;
    int rowId, selVal;
    Cursor c;
    EditTextBackEvent etName;
    NumberPicker npWid, npLen, npSpans;
    MatSpanModel newSpan = new MatSpanModel();
    TextView tvSummary;
    SegmentedGroup segLay, segStart;
    RadioButton btnBw, btn21, btn346, btn3412;
    RadioButton btn6, btn12;
    Boolean dataChanged = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_span_edit);

        etName = (EditTextBackEvent) findViewById(R.id.et_name);
        npWid = (NumberPicker) findViewById(R.id.npWid);
        npLen = (NumberPicker) findViewById(R.id.npLen);
        npSpans = (NumberPicker)findViewById(R.id.npSpans);
        tvSummary = (TextView)findViewById(R.id.tvSummary);
        segLay = (SegmentedGroup)findViewById(R.id.segmentLay);
        segStart = (SegmentedGroup)findViewById(R.id.segmentStart);
        btn6 = (RadioButton)findViewById(R.id.btn6);
        btn12 = (RadioButton)findViewById(R.id.btn12);
        btnBw = (RadioButton)findViewById(R.id.btnBw);
        btn21 = (RadioButton)findViewById(R.id.btn21);
        btn346 = (RadioButton)findViewById(R.id.btn346);
        btn3412 = (RadioButton)findViewById(R.id.btn3412);

        npWid.setMinValue(0); // Min value of 6 is enforced by listener
        npWid.setMaxValue(1500);
        npLen.setMinValue(0); // Min value of 2 is enforced by listener
        npLen.setMaxValue(20000);
        npSpans.setMinValue(1);
        npSpans.setMaxValue(99);

        // Grab the selected data and put it on the screen and into a MatSpanModel for calculations
        Bundle showData = getIntent().getExtras();
        rowId = showData.getInt("keyid");
        adapter = new MatSpanDbAdapter(this);
        c = adapter.querySpan(rowId);
        if (c.moveToFirst()) {
            do {
                newSpan.setName(c.getString(1));
                etName.setText(c.getString(1));
                newSpan.setWid(c.getInt(2));
                npWid.setValue(c.getInt(2));
                newSpan.setLen(c.getInt(3));
                npLen.setValue(c.getInt(3));
                newSpan.setQty(c.getInt(4));
                npSpans.setValue(c.getInt(4));
                newSpan.setLay(c.getInt(5));
                segLay.check(c.getInt(5));
                newSpan.setStart(c.getInt(6));
                segStart.check(c.getInt(6));
                selVal = c.getInt(7);
            } while (c.moveToNext());
        }
        switch (newSpan.getLay()) {
            case 0:
                btnBw.setSelected(true);
                btnBw.setChecked(true);
                break;
            case 1:
                btn21.setSelected(true);
                btn21.setChecked(true);
                break;
            case 2:
                btn346.setSelected(true);
                btn346.setChecked(true);
                break;
            case 3:
                btn3412.setSelected(true);
                btn3412.setChecked(true);
                break;
        }
        if (newSpan.getStart()) {
            btn12.setSelected(true);
            btn12.setChecked(true);
        } else {
            btn6.setSelected(true);
            btn6.setChecked(true);
        }

        // Set up Name text listeners
        etName.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextBackEvent ctrl, String text) {
                if (dataChanged) {
                    newSpan.setName(etName.getText().toString());
                    refreshData();
                }
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && dataChanged) {
                    // if lost focus, update name entry.
                    EditText editText = (EditText) view;
                    newSpan.setName(editText.getText().toString());
                    refreshData();
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
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                // Trying to catch when the text is actually changed
                dataChanged=true;
            }
        });
        // Set up number picker for Width
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
                refreshData();
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
                refreshData();
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
        npSpans.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                newSpan.setQty(newVal);
                refreshData();
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
        // Set up listeners for Segmented selectors
        segLay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int layVal = segLay.indexOfChild(findViewById(segLay.getCheckedRadioButtonId()));
                newSpan.setLay(layVal);
                refreshData();
            }
        });
        segStart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int startVal = segStart.indexOfChild(findViewById(segStart.getCheckedRadioButtonId()));
                newSpan.setStart(startVal);
                refreshData();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update span values and return to list
                if (dataChanged) {
                    saveData();
                } else {
                    Toast.makeText(MatSpanEditActivity.this, "No Changes", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void refreshData(){
        newSpan.calcMat();
        tvSummary.setText(newSpan.summarize());
        dataChanged = true;
    }

    private void saveData(){
        String nameVal = etName.getText().toString();
        int widVal = npWid.getValue();
        int lenVal = npLen.getValue();
        int spansVal = npSpans.getValue();
        int layVal = segLay.indexOfChild(findViewById(segLay.getCheckedRadioButtonId()));
        int startVal = segStart.indexOfChild(findViewById(segStart.getCheckedRadioButtonId()));
        adapter.updateSpan(rowId, nameVal, widVal, lenVal, spansVal, layVal, startVal, selVal);
        Toast.makeText(MatSpanEditActivity.this, "Span Saved", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        // Update span values and return to list
        if (dataChanged) {
            saveData();
        }
        finish();
    }
}

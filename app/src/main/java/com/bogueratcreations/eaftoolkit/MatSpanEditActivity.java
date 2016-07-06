package com.bogueratcreations.eaftoolkit;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

//import com.bogueratcreations.eafToolkit.R;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import info.hoang8f.android.segmented.SegmentedGroup;

public class MatSpanEditActivity extends AppCompatActivity {

    MatSpanDbAdapter adapter;
    MatSpanDbHelper helper;
    int rowId;
    Cursor c;
    String nameVal, widVal, lenVal, spansVal;
    EditText etName, etWid, etLen, etSpans;
    Button editSubmit, btnDelete;
    SegmentedGroup segLay, segStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_span_edit);

        etName = (EditText) findViewById(R.id.et_name);
        etWid = (EditText) findViewById(R.id.et_wid);
        etLen = (EditText) findViewById(R.id.et_len);
        etSpans = (EditText) findViewById(R.id.et_spans);
        editSubmit = (Button) findViewById(R.id.btn_update);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        segLay = (SegmentedGroup)findViewById(R.id.segmentLay);
        segStart = (SegmentedGroup)findViewById(R.id.segmentStart);

        Bundle showData = getIntent().getExtras();
        rowId = showData.getInt("keyid");
        // Toast.makeText(getApplicationContext(), Integer.toString(rowId),
        // 500).show();
        adapter = new MatSpanDbAdapter(this);

        c = adapter.querySpan(rowId);

        if (c.moveToFirst()) {
            do {
                etName.setText(c.getString(1));
                etWid.setText(c.getString(2));
                etLen.setText(c.getString(3));
                etSpans.setText(c.getString(4));

            } while (c.moveToNext());
        }

        btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                adapter.deleteRecord(rowId);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save action initiated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                adapter.updateSpan(rowId,
                        etName.getText().toString(),
                        Integer.getInteger(etWid.getText().toString()),
                        Integer.getInteger(etLen.getText().toString()),
                        Integer.getInteger(etSpans.getText().toString()),
                        segLay.indexOfChild(findViewById(segLay.getCheckedRadioButtonId())),
                        segStart.indexOfChild(findViewById(segStart.getCheckedRadioButtonId())),
                        0);  // TODO: add check for selected
                finish();
            }
        });
    }

}

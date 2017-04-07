package com.bogueratcreations.eaftoolkit.MatSpan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.R;

import java.io.IOException;

public class MatSpanSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_span_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MatSpanDbAdapter adapter = new MatSpanDbAdapter(this);
        final String summary = adapter.summarizeSelected();
        // put summary in text box
        TextView tvSummary = (TextView)findViewById(R.id.tvMatSummary);
        tvSummary.setText(summary);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    emailSummary(summary);
                } catch (IOException ioe) {
                    Toast.makeText(getApplicationContext(),"Email Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void emailSummary(String summary) throws IOException {

        //MatSpanDbAdapter adapter = new MatSpanDbAdapter(this);
        //String summary = adapter.summarizeSelected();

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "EAF Toolkit - Matting Summary");
        sendIntent.putExtra(Intent.EXTRA_TEXT, summary);
        sendIntent.setType("text/html");
        try {
            startActivity(sendIntent);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}

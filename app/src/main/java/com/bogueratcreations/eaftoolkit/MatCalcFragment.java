package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Formatter;

public class MatCalcFragment extends Fragment {

    EditText etWidth;
    EditText etLength;
    TextView results1;
    TextView results2;
    TextView results3;
    TextView results4;
    Switch swStartLay;
    Spinner spPattern;
    Button btnCalcMat;

    public MatCalcFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_mat_calc, container, false);
        etWidth = (EditText)V.findViewById(R.id.etWidth);
        etLength = (EditText)V.findViewById(R.id.etLength);
        swStartLay = (Switch)V.findViewById(R.id.swStartLay);
        spPattern = (Spinner)V.findViewById(R.id.spLay);
        btnCalcMat = (Button)V.findViewById(R.id.btnCalcMat);
        results1 = (TextView)V.findViewById(R.id.tvResults1);
        results2 = (TextView)V.findViewById(R.id.tvResults2);
        results3 = (TextView)V.findViewById(R.id.tvResults3);
        results4 = (TextView)V.findViewById(R.id.tvResults4);

        etWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Clear the results when changes are made.
                results1.setText("");
                results2.setText("");
                results3.setText("");
                results4.setText("");
            }
        });

        etLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Clear the results when changes are made.
                results1.setText("");
                results2.setText("");
                results3.setText("");
                results4.setText("");
            }
        });
        btnCalcMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect Values from View
                if ((etWidth.length() == 0) || (etLength.length() == 0)) {
                    Toast.makeText(getActivity(), "Entry required for Width and Length", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Integer width = Integer.parseInt(etWidth.getText().toString());
                final Integer length = Integer.parseInt(etLength.getText().toString());
                final Boolean startWith12 = swStartLay.isChecked();
                // pattern: 0 - brickwork; 1 - 2-1; 2 - 3/4 Lay max 6; 3 - 3/4 Lay Max 12
                final Integer pattern = spPattern.getSelectedItemPosition();

                if ((width > 0) && (length > 0)) {
                    // Validate Width and Length
                    if ((width % 6 > 0) || (length % 2 > 0)) {
                        Toast.makeText(getActivity(), "Width must be divisible by 6 and Length by 2; and greater than zero!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Calculate stuff here
                    Integer sixes = CalcMat.getMat6(length, width, startWith12, pattern, 0, 0, 0, 0);
                    Integer twelves = CalcMat.getMat12(length, width, startWith12, pattern, 0, 0, 0, 0);
                    Integer sixRack = Integer.valueOf(CalcMat.getPallet(sixes, 162));
                    Integer tweRack = Integer.valueOf(CalcMat.getPallet(twelves, 162));

                    results1.setText(twelves.toString() + " / " + sixes.toString());
                    results2.setText(CalcMat.getPallet(twelves, 18) + " / " + CalcMat.getPallet(sixes, 18));
                    if (sixRack == null || tweRack == null) {
                    }
                    if (sixRack > tweRack) {
                        results3.setText(sixRack.toString());
                    } else {
                        results3.setText(tweRack.toString());
                    }
                    Integer SqFt = length * width;
                    String strSqFt;
                    strSqFt = new DecimalFormat("#,###,##0").format(SqFt);
                    results4.setText(strSqFt);
                } else {
                    Toast.makeText(getActivity(), "Width must be divisible by 6 and Length by 2; and greater than zero!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return V;
    }

}

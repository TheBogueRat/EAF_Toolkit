package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class MatCalcFragment extends Fragment {

    EditText etWidth;
    EditText etLength;
    TextView result1;
    TextView result2;
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
        result1 = (TextView)V.findViewById(R.id.tvResult1);
        result2 = (TextView)V.findViewById(R.id.tvResult2);

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
                result1.setText("");
                result2.setText("");
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
                result1.setText("");
                result2.setText("");
            }
        });
        btnCalcMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect Values from View
                if ((etWidth.length() == 0)||(etLength.length() == 0)) {
                    Toast.makeText(getActivity(),"Entry required for Width and Length", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Integer width = Integer.parseInt(etWidth.getText().toString());
                final Integer length = Integer.parseInt(etLength.getText().toString());
                final Boolean startWith12 = swStartLay.isChecked();
                // pattern: 0 - brickwork; 1 - 2-1; 2 - 3/4 Lay max 6; 3 - 3/4 Lay Max 12
                final Integer pattern = spPattern.getSelectedItemPosition();

                if((width > 0)&&(length > 0)) {
                    // Validate Width and Length
                    if ((width % 6 > 0)||(length % 2 > 0)) {
                        Toast.makeText(getActivity(),"Width must be divisible by 6 and Length by 2; and greater than zero!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Calculate stuff here
                    Integer sixes = CalcMat.getMat6(length, width, startWith12, pattern, 0, 0, 0, 0);
                    Integer twelves = CalcMat.getMat12(length, width, startWith12, pattern, 0, 0, 0, 0);

                    result1.setText("12' Sheets: " + twelves + " and 6' Sheets: " + sixes.toString());
                    result2.setText("F71: " + CalcMat.getPallet(twelves, 18) + " - F72: " + CalcMat.getPallet(sixes, 18));
                } else {
                    Toast.makeText(getActivity(),"Width must be divisible by 6 and Length by 2; and greater than zero!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        return V;
    }

}

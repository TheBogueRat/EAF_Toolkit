package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlopeFragment1 extends Fragment{

    EditText etElev;
    EditText etDist;
    TextView tvResult;

    public SlopeFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_slope1, container, false);
        tvResult = (TextView)V.findViewById(R.id.tvSlope1Result);
        etElev = (EditText)V.findViewById(R.id.etSlope1Elev);
        etDist = (EditText)V.findViewById(R.id.etSlope1Dist);
        etElev.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Don't show results if either EditText is empty
                if ((s.toString().length() == 0) || (etDist.getText().toString().length() == 0)){
                    tvResult.setText("");
                    return;
                }
                Double rise = Double.parseDouble(s.toString());
                Double run = Double.parseDouble(etDist.getText().toString());
                Double percent = (rise / run) * 100.0;
                percent = Math.round(percent * 100.0) / 100.0;
                String degrees = convert.Percent2Degrees(percent);
                String percent2 = percent.toString();
                tvResult.setText(percent2 + "% / " + degrees + (char) 0x00B0);
            }
        });
        etDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Don't show results if either EditText is empty
                if ((s.toString().length() == 0) || (etElev.getText().toString().length()) == 0){
                    tvResult.setText("");
                    return;
                }
                Double rise = Double.parseDouble(etElev.getText().toString());
                Double run = Double.parseDouble(s.toString());
                Double percent = (rise / run) * 100.0;
                percent = Math.round(percent * 100.0) / 100.0;
                String degrees = convert.Percent2Degrees(percent);
                String percent2 = percent.toString();
                tvResult.setText(percent2 + "% / " + degrees + (char) 0x00B0);
            }
        });

        return V;
    }

}


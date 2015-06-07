package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlopeFragment2 extends Fragment {

    EditText etElev1;
    EditText etElev2;
    EditText etDist;
    TextView tvResult;

    public SlopeFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_slope2, container, false);
        tvResult = (TextView)V.findViewById(R.id.tvSlope2Result);
        etElev1 = (EditText)V.findViewById(R.id.etSlope2Elev1);
        etElev2 = (EditText)V.findViewById(R.id.etSlope2Elev2);
        etDist = (EditText)V.findViewById(R.id.etSlope2Dist);
        // TODO: Go back and create a method (function) to handle this since all 3 are basically the same.
        etElev1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String elev1 = s.toString();
                String elev2 = etElev2.getText().toString();
                String dist = etDist.getText().toString();
                Double rise;
                // Don't show results if either EditText is empty
                if ((elev1.length() == 0) || (dist.length() == 0) || (elev2.length() == 0)){
                    tvResult.setText("");
                    return;
                }
                Double elev1dbl = Double.parseDouble(elev1);
                Double elev2dbl = Double.parseDouble(elev2);
                if (elev1dbl > elev2dbl) {
                    rise = elev1dbl - elev2dbl;
                } else {
                    rise = elev2dbl - elev1dbl;
                }
                Double run = Double.parseDouble(dist);
                Double percent = (rise / run) * 100;
                String degrees = convert.Percent2Degrees(percent);
                String percent2 = percent.toString();
                tvResult.setText(percent2 + "% / " + degrees + (char) 0x00B0);
            }
        });
        etElev2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String elev1 = etElev1.getText().toString();
                String elev2 = s.toString();
                String dist = etDist.getText().toString();
                Double rise;
                // Don't show results if either EditText is empty
                if ((elev1.length() == 0) || (dist.length() == 0) || (elev2.length() == 0)){
                    tvResult.setText("");
                    return;
                }
                Double elev1dbl = Double.parseDouble(elev1);
                Double elev2dbl = Double.parseDouble(elev2);
                if (elev1dbl > elev2dbl) {
                    rise = elev1dbl - elev2dbl;
                } else {
                    rise = elev2dbl - elev1dbl;
                }
                Double run = Double.parseDouble(dist);
                Double percent = (rise / run) * 100;
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
                String elev1 = etElev1.getText().toString();
                String elev2 = etElev2.getText().toString();
                String dist = s.toString();
                Double rise;
                // Don't show results if either EditText is empty
                if ((elev1.length() == 0) || (dist.length() == 0) || (elev2.length() == 0)){
                    tvResult.setText("");
                    return;
                }
                Double elev1dbl = Double.parseDouble(elev1);
                Double elev2dbl = Double.parseDouble(elev2);
                if (elev1dbl > elev2dbl) {
                    rise = elev1dbl - elev2dbl;
                } else {
                    rise = elev2dbl - elev1dbl;
                }
                Double run = Double.parseDouble(dist);
                Double percent = (rise / run) * 100;
                String degrees = convert.Percent2Degrees(percent);
                String percent2 = percent.toString();
                tvResult.setText(percent2 + "% / " + degrees + (char) 0x00B0);
            }
        });
        return V;
    }

}

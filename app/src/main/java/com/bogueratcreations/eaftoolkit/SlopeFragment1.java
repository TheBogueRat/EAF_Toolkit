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
        etElev = (EditText)V.findViewById(R.id.etSlope1Elev);
        etElev.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvResult.setText(s);
            }
        });
        etDist = (EditText)V.findViewById(R.id.etSlope1Dist);
        etDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvResult.setText(s);
            }
        });
        tvResult = (TextView)V.findViewById(R.id.tvSlope1Result);

        return V;
    }

}


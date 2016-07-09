package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlopeFragment3 extends Fragment {

    private EditText etPer;
    private EditText etDeg;
    private Boolean editing = false;

    public SlopeFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_slope3, container, false);
        etPer = (EditText)V.findViewById(R.id.etSlope3Per);
        etPer.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etPer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(editing){return;} // The other control is making changes
                editing = true;
                if (s.toString().length() == 0){
                    etDeg.setText("");
                    editing = false;
                    return;
                }
                Double percent = Double.parseDouble(s.toString());
                if (percent > 100){
                    etPer.setError("While this could be converted, an EAF Specialist will not likely need to find a percentage for an angle over 45 degrees.");
                    etDeg.setText("");
                    editing = false;
                    return;
                } else {
                    if (etPer.getError() != null) {etPer.setError(null);}
                }
                etDeg.setText(convert.Percent2Degrees(percent));
                editing = false;
            }
        });
        etDeg = (EditText)V.findViewById(R.id.etSlope3Deg);
        etDeg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(editing){return;} // The other control is making changes
                editing = true; // Because I'm going to make changes
                if (s.toString().length() == 0){
                    etPer.setText("");
                    editing = false;
                    return;
                }
                Double degrees = Double.parseDouble(s.toString());
                if (degrees > 90){
                    etDeg.setError("While this could be converted, an EAF Specialist will not likely need to find a percentage for an angle this large.");
                    etPer.setText("");
                    editing = false;
                    return;
                } else {
                   if (etDeg.getError() != null) {etDeg.setError(null);}
                }
                etPer.setText(convert.Degrees2Percent(degrees));
                editing = false;
            }
        });

        return V;
    }

}

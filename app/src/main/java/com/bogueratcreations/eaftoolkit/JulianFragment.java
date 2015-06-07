package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * A placeholder fragment containing a simple view.
 */
public class JulianFragment extends Fragment {

    DatePicker dp;
    EditText etJulian;
    TextView tvSync;
    Calendar cal;
    Boolean editing = false;

    public JulianFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_julian, container, false);
        etJulian = (EditText)V.findViewById(R.id.etJulian);
        tvSync = (TextView)V.findViewById(R.id.tvSync);
        dp = (DatePicker)V.findViewById(R.id.datePicker);

        cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        etJulian.setText(convert.cal2Julian(cal));

        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (editing) {
                    return;
                }
                // Remove Error messages from view when datePicker is used.
                if (tvSync.getVisibility() == View.VISIBLE) {
                    tvSync.setVisibility(View.INVISIBLE);
                }
                if (etJulian.getError() != null) {
                    etJulian.setError(null);
                }
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                editing = true;
                etJulian.setText(convert.cal2Julian(cal));
                editing = false;
            }
        });

        etJulian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (editing){return;}
                // Check to ensure length is 4 else exit
                if (s.toString().length() != 4) {
                    if (tvSync.getVisibility() == View.INVISIBLE){tvSync.setVisibility(View.VISIBLE);}
                    return;
                }
                if (tvSync.getVisibility() == View.VISIBLE){tvSync.setVisibility(View.INVISIBLE);}
                int day = Integer.parseInt(s.toString().substring(1));
                if ((day < 1) || (day > 366)){
                    // Make error
                    etJulian.setError("The DDD portion must be between 001 and 366.");
                    if (tvSync.getVisibility() == View.INVISIBLE){tvSync.setVisibility(View.VISIBLE);}
                } else {
                    if (etJulian.getError() != null){etJulian.setError(null);}
                }
                String yearDigit = s.toString().substring(0, 1);
                Calendar cal = Calendar.getInstance();
                int year = Integer.valueOf(yearDigit) + ((dp.getYear() / 10) * 10);
                cal.set(year, 1, 1);
                Log.e("Day of year: ", String.valueOf(day));
                // zero based so day less one.
                cal.add(Calendar.DATE, day - 1);
                editing = true;
                dp.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cal.get(Calendar.DATE));
                editing = false;
            }
        });


        return V;
    }

}

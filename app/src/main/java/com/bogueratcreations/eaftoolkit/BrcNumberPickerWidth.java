package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BrcNumberPickerWidth extends android.widget.NumberPicker {

    private EditText eText;

    public BrcNumberPickerWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initEditText(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initEditText(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        initEditText(child);
    }

    public void initEditText(View view){
        if(view instanceof EditText){
            final EditText eText = (EditText) view;
            eText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Toast.makeText(getContext(), "onFocusChange fired. Focus: " + b + " eText.Text: " + eText.getText(), Toast.LENGTH_SHORT).show();
                    if (!b) {
                        setValue(Integer.parseInt(eText.getText().toString()));
                    }
//                    if (newVal<12) {
//                        newVal = 12;
//                    } else if (Math.abs(newVal-oldVal)>6) {
//                        // Assume the user typed in a new value more than a 6 step, Ensure it is divisible by 6
//                        newVal = newVal/6 *6;
//                        // Update the view
//                    } else {
//                        newVal = (newVal < oldVal) ? oldVal - 6 : oldVal + 6;
//                    }
                }
            });
            eText.addTextChangedListener(new TextWatcher(){
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

//                    try{
//                        int num = Integer.parseInt(s.toString());
//                        setValue(num); //this line changes the value of your numberpicker
//                    }catch(NumberFormatException E){
//                        //do your catching here
//                    }
                }});
        }
    }
}

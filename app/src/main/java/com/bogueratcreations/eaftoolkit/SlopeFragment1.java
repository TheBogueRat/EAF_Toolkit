package com.bogueratcreations.eaftoolkit;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlopeFragment1 extends Fragment {

    public SlopeFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (added Layout type at start)
        return (RelativeLayout) inflater.inflate(R.layout.fragment_slope1, container, false);
    }

}

package com.bogueratcreations.eaftoolkit;

import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogueratcreations.eaftoolkit.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramFragment extends ListFragment {

    private QuestionsFragment.OnFragmentInteractionListener mListener;

    public ProgramFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_program, container, false);
    }
}

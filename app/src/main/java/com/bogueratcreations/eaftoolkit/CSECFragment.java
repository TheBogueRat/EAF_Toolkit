package com.bogueratcreations.eaftoolkit;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bogueratcreations.eaftoolkit.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class CSECFragment extends Fragment {

    ToolkitDBhelper mDb;
    ArrayList mMenuItems;

    public CSECFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_flols, container, false);


        return V;
    }
}

package com.bogueratcreations.eaftoolkit.Inspections;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ListView;

import com.bogueratcreations.eaftoolkit.Inspections.Programs;
import com.bogueratcreations.eaftoolkit.ListViewItem;
import com.bogueratcreations.eaftoolkit.ListViewItemAdapter;
import com.bogueratcreations.eaftoolkit.Prefs;
import com.bogueratcreations.eaftoolkit.R;

import java.util.ArrayList;
import java.util.List;

public class InspectFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the itmes list
        List<ListViewItem> mItems = new ArrayList<>();
        Resources resources = getResources();

        mItems.add(new ListViewItem(ResourcesCompat.getDrawable(resources, R.drawable.inspectcsec, null), "NAMP - CSEC", "Computerized Self-Evaluation Checklist"));
        mItems.add(new ListViewItem(ResourcesCompat.getDrawable(resources, R.drawable.inspecteaf, null), "IG Checklist - EAF", "FAI dtd February 2016"));
        mItems.add(new ListViewItem(ResourcesCompat.getDrawable(resources, R.drawable.inspectarff, null), "IG Checklist - ARFF", "FAI dtd April 2016"));

        // Initialize and set the list adapter
        setListAdapter(new ListViewItemAdapter(getActivity(), mItems));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve the listview item
        //ListViewItem item = mItems.get(position);

        // Prepare intent
        Intent intent = new Intent(getActivity(), Programs.class);

        String message;
        // Then go to new activity, perhaps change Toast to Loading....
        switch (position) {
            case 0: // CSEC
                message = "CSEC";
                break;
            case 1: // CGRI EAF
                message = "EAF";
                break;
            case 2: // CGRI ARFF
                message = "ARFF";
                break;
            default:
                message = "CSEC";
        }

        Prefs.writeString(getActivity().getApplicationContext(), "TABLE", message);
        // intent.putExtra(Inspect.EXTRA_MESSAGE, message);

        startActivity(intent);

    }
}

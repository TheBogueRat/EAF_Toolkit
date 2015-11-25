package com.bogueratcreations.eaftoolkit;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InspectFragment extends ListFragment {

    private List<ListViewItem> mItems;  // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the itmes list
        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.inspectcsec), "NAMP - CSEC", "Computerized Self-Evaluation Checklist"));
        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.inspecteaf), "CGRI - EAF", "dtd October 2015"));
        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.inspectarff), "CGRI - ARFF", "dtd December 2014"));

        // Initialize and set the list adapter
        setListAdapter(new ListViewItemAdapter(getActivity(), mItems));
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Customize the view?
        // getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve the listview item
        ListViewItem item = mItems.get(position);

        // Prepare intent
        Intent intent = new Intent(getActivity(), Programs.class);

        String message = "";
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

package com.bogueratcreations.eaftoolkit;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.imgGallery.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChkListFragment extends ListFragment {

    private List<ListViewItem> mItems;  // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the itmes list
        mItems = new ArrayList<ListViewItem>();
        Resources resources = getResources();

        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.forks), "NAMP - CSEC", "Computerized Self-Evaluation Checklist"));
        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.slope), "CGRI - EAF", "dtd August 2012"));
        mItems.add(new ListViewItem(resources.getDrawable(R.drawable.calendar), "CGRI - ARFF", "dtd December 2014"));

        // Initialize and set the list adapter
        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
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

        // Do Something with it (pass to next)
        Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();

        // Then go to new activity, perhaps change Toast to Loading....
        switch (position) {
            case 0: // CSEC
                startActivity(new Intent(getActivity(), Inspections.class));
                break;
            case 1: // CGRI EAF
                startActivity(new Intent(getActivity(), Inspections.class));
                break;
            case 2: // CGRI ARFF
                startActivity(new Intent(getActivity(), Inspections.class));
                break;
        }
    }
}

package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CSECFragment extends Fragment {

    private ListView lv;

    public CSECFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.fragment_csec, container, false);

        // Is working code for single line entry list view showing _id only
        lv = (ListView) V.findViewById(R.id.lvCSEC);

        DbAccess dba = DbAccess.getInstance(getActivity());
        dba.open();
        List<String> questions = dba.getQuestions();
        dba.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,questions);
        lv.setAdapter(adapter);


        // TODO: Incorporate TODO database cursor...
        // Find ListView to populate
        ListView lvItems = (ListView) V.findViewById(R.id.lvCSEC);
        // Setup cursor adapter using cursor from last step
        //ToolkitCursorAdapter todoAdapter = new ToolkitCursorAdapter(getActivity(), dba);
        // Attach cursor adapter to the ListView
        //lvItems.setAdapter(todoAdapter);

        return V;
    }


}

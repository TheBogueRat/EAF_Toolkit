package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuestionsFragment extends Fragment {

    public QuestionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        // Retrieve the program for the sending activity/fragment?
        //Intent intent = getActivity().getIntent();
        //String message = intent.getStringExtra(Programs.EXTRA_MESSAGE);
        //String tableName = intent.getStringExtra(Programs.EXTRA_MESSAGE_TABLE);
        String message = Prefs.readString(getActivity().getApplicationContext(), "PROGRAM", "");
        String tableName = Prefs.readString(getActivity().getApplicationContext(), "TABLE", "");
        //String message = "200  Maintenance Training";
        ListView listView = (ListView) v.findViewById(R.id.lvQuestions);
        InspectDBHandler handler = new InspectDBHandler(getActivity().getApplicationContext());
        handler.setTableName(tableName);
        ArrayList<Question> questionList = handler.getQuestions(message);
        QuestionAdapter adapter = new QuestionAdapter(getActivity().getApplicationContext(), questionList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO: Navigate properly to next level (Not usign detail view yet)
                //Intent intent = new Intent(getActivity(), QuestionDetail.class);
                //startActivity(intent);
            }
        });

        return v;
    }
}

package com.bogueratcreations.eaftoolkit.Inspections;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.Prefs;
import com.bogueratcreations.eaftoolkit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramsFragment extends Fragment {

    private ListView listView;
    private ProgramAdapter adapter;
    private InspectDBHandler handler;
    // For displaying the Programs
    ArrayList<Program> programArrayList;

    private ProgressDialog pDialog;

    //public static final String PREFS_NAME = "BRCprefs";

    public ProgramsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_programs, container, false);
        //setContentView(R.layout.activity_main);
        listView = (ListView) v.findViewById(R.id.lvProgram);
        handler = new InspectDBHandler(getActivity().getApplicationContext());

        // Retrieve the table name that was passed from the Inspect.class
        // Get the message from the intent
        // TODO: only retrieve from initial navigation from Inspect, not back-nav from Question (revisit when complete, may be problem with Question back-nav)
        //Intent intent = getActivity().getIntent();

        //String message = intent.getStringExtra(Inspect.EXTRA_MESSAGE);
        String message = Prefs.readString(getActivity().getApplicationContext(), "TABLE", "");
        // TODO: Handle NULL when going back using menu (creating a new fragment?) Loses track of which checklist we are using.
        if (message == null) { message = "";}
        // Alternative?
        //String Item = getActivity().getIntent().getExtras().getString("name");
        // Set the table name for the handler
        String tableName = "CSEC";
        if ((message.equals("CSEC")) || (message.equals("EAF")) || (message.equals("ARFF"))) {
            tableName = message;
        }
        else { }  // TODO: Handle issue with not receiving a valid option
        Log.i("EAF Toolkit", "Using table '" + tableName + "'  " + "Message value is: " + message);
        handler.setTableName(tableName);

        // Check for questions in the DB and populate as necessary.
        if(handler.getQuestionCount() == 0)
        {
            // Insert JSON Data into the SQL database.
            new DataFetcherTask().execute();

        } else {
            ArrayList<Program> programList = handler.getPrograms();
            adapter = new ProgramAdapter(getActivity().getApplicationContext(), programList);
            listView.setAdapter(adapter);
        }

        // This was a suggested workaround to grab the tableName
        //final String finalTableName = tableName;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Navigate properly to next level
                Intent intent = new Intent(getActivity(), Questions.class);
                // TODO: The message to send program selection to the Questions Activity (Position is zero-based)

                TextView programText = (TextView) view.findViewById(R.id.tvProgram);
                String message = programText.getText().toString();
                Log.i("EAF Toolkit", "Sending Program: " + message);
                //intent.putExtra(Programs.EXTRA_MESSAGE, message);
                //intent.putExtra(Programs.EXTRA_MESSAGE_TABLE, finalTableName);

                // TODO Save current inspection and table name rather than sending due to back nav issues.
                Prefs.writeString(getActivity().getApplicationContext(), "PROGRAM", message);

                startActivity(intent);
            }
        });
        return v;
    }

    // Retrieves data from JSON and inserts it into the DB if it doesn't exist.
    private class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog  (chaged from MainActivity.this to getActivity)
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading New Inspection Data...Gimme a minute...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject;

            // Retrieve CSEC JSON Data
            jsonObject = parseJSONData("CSEC.json");
            handler.setTableName("CSEC");
            // Push into DB
            //ArrayList<Question> questionArrayList;
            try {
                //questionArrayList = new ArrayList<Question>();
                // Get the Array of all programs for the current checklist
                JSONArray jsonArray = jsonObject.getJSONArray("Checklist");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectProgram = jsonArray.getJSONObject(i);
                    String progNum = jsonObjectProgram.getString("ProgramID");
                    String program = jsonObjectProgram.getString("ProgramName");
                    String qNum = jsonObjectProgram.getString("Qnum");
                    String questionItem = jsonObjectProgram.getString("Question");
                    String reference = jsonObjectProgram.getString("Reference");
                    Question question = new Question();
                    question.setProgram(progNum + "  " + program);
                    question.setQuestion(qNum + " - " + questionItem);
                    question.setReference("Reference: " + reference);
                    handler.addQuestion(question);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Retrieve CSEC JSON Data
            jsonObject = parseJSONData("EAF.json");
            handler.setTableName("EAF");
            // Push into DB
            try {
                // Get the Array of all programs for the current checklist
                JSONArray jsonArray = jsonObject.getJSONArray("Checklist");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectProgram = jsonArray.getJSONObject(i);
                    String progNum = jsonObjectProgram.getString("ProgramID");
                    String program = jsonObjectProgram.getString("ProgramName");
                    String qNum = jsonObjectProgram.getString("Qnum");
                    String questionItem = jsonObjectProgram.getString("Question");
                    String reference = jsonObjectProgram.getString("Reference");
                    Question question = new Question();
                    question.setProgram(progNum + "  " + program);
                    question.setQuestion(qNum + " - " + questionItem);
                    question.setReference("Reference: " + reference);
                    handler.addQuestion(question);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Retrieve CSEC JSON Data
            jsonObject = parseJSONData("ARFF.json");
            handler.setTableName("ARFF");
            // Push into DB
            try {
                // Get the Array of all programs for the current checklist
                JSONArray jsonArray = jsonObject.getJSONArray("Checklist");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectProgram = jsonArray.getJSONObject(i);
                    String progNum = jsonObjectProgram.getString("ProgramID");
                    String program = jsonObjectProgram.getString("ProgramName");
                    String qNum = jsonObjectProgram.getString("Qnum");
                    String questionItem = jsonObjectProgram.getString("Question");
                    String reference = jsonObjectProgram.getString("Reference");
                    Question question = new Question();
                    question.setProgram(progNum + "  " + program);
                    question.setQuestion(qNum + " - " + questionItem);
                    question.setReference("Reference: " + reference);
                    handler.addQuestion(question);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Json Parsing code end, Db has been updated onPostExecute will refresh listView
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Data refresh completed, update ListView
            String tableName = "CSEC";
            handler.setTableName(tableName);
            ArrayList<Program> programList = handler.getPrograms();
            adapter = new ProgramAdapter(getActivity().getApplicationContext(), programList);
            listView.setAdapter(adapter);

            // Dismiss the progress dialog
            if (pDialog.isShowing()) pDialog.dismiss();

        }
    }

    //Method that will parse the JSON file and will return a JSONObject
    private JSONObject parseJSONData(String jsonFile) {
        String JSONString;
        JSONObject JSONObject;

        Log.i("EAFtoolkit", "Parsing JSON Data from file: " + jsonFile);
        try {
            InputStream inputStream =
                    getActivity().getApplicationContext().getAssets().open(jsonFile);
            //open the inputStream to the file (added getActivity())
            //InputStream inputStream = context.getAssets().open(jsonFile);

            int sizeOfJSONFile = inputStream.available();

            //array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            //reading data into the array from the file
            inputStream.read(bytes);

            //close the input stream
            inputStream.close();

            JSONString = new String(bytes, "UTF-8");
            JSONObject = new JSONObject(JSONString);

        } catch (IOException ex) {

            ex.printStackTrace();
            return null;
        } catch (JSONException x) {
            x.printStackTrace();
            return null;
        }
        return JSONObject;
    }

}

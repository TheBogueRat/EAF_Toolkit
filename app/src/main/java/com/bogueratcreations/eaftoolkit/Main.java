package com.bogueratcreations.eaftoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main extends AppCompatActivity {

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] listIcons = new int[]{
            R.drawable.forks,
            R.drawable.slope,
            R.drawable.icon,
            R.drawable.flols,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon
    };

    // Array of strings storing country names
    String[] listTitles = new String[] {
            "Matting Calculator",
            "Slope Converter",
            "Julian Dater",
            "FLOLS H/E",
            "CGRI/FAI",
            "CSEC",
            "NAMP Codes",
            "DCP (TBD)",
            "Images",
            "About"
    };

    // Array of strings to store currencies
    String[] listDesc = new String[]{
            "Calculate Matting Requirements",
            "Get the Slope in Percent or Degrees",
            "Convert to/from Julain and Gregorian Calendars",
            "Calculate Roll Angle and Pole Height",
            "Inspection checklist from HQMC website",
            "NAMP Computerized Self Examination Checklists",
            "Maintenance Codes",
            "CBR Calculator in the works",
            "Random EAF Pictures",
            "About the EAF Toolkit"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<>();

        for(int i=0;i<10;i++){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("listTitles", listTitles[i]);
            hm.put("listDesc", listDesc[i]);
            hm.put("listIcons", Integer.toString(listIcons[i]) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "listIcons","listTitles","listDesc" };

        // Ids of views in listview_layout
        int[] to = { R.id.listIcons,R.id.listTitles,R.id.listDesc};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        // Item Click Listener for the listview
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Getting the Container Layout of the ListView
                LinearLayout linearLayoutParent = (LinearLayout) container;
                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);
                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutChild.getChildAt(0);

                switch (position) {
                    case 0: // Matting Calculator
                        startActivity(new Intent(Main.this, MatCalc.class));
                        break;
                    case 1: // Slope
                        startActivity(new Intent(Main.this, Slope.class));
                        break;
                    case 2: // Julian
                        startActivity(new Intent(Main.this, Julian.class));
                        break;
                    case 3:  // FLOLS
                        startActivity(new Intent(Main.this, FLOLS.class));
                        break;
                    case 4:  // CGRI
                        startActivity(new Intent(Main.this, Inspections.class));
                        break;
                    case 5:  // CSEC
                        startActivity(new Intent(Main.this, Inspections.class));
                        break;

                }
            }
        };


        // Setting the item click listener for the listview
        listView.setOnItemClickListener(itemClickListener);
        /*
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, Inspections.class));
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

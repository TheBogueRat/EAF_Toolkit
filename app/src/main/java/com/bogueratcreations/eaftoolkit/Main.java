package com.bogueratcreations.eaftoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bogueratcreations.eaftoolkit.imgGallery.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main extends AppCompatActivity {

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] listIcons = new int[]{
            R.drawable.forks,
            R.drawable.slope,
            R.drawable.calendar,
            R.drawable.flols,
            R.drawable.inspect,
            R.drawable.br,
            R.drawable.brc
    };

    // Array of strings storing country names
    String[] listTitles = new String[] {
            "Matting Calculator",
            "Slope Converter",
            "Julian Dater",
            "FLOLS H/E",
            "Inspections",
            "Images",
            "About"
    };
/*
            "CGRI/FAI",
            "CSEC",
            "NAMP Codes",
            "DCP (TBD)",
 */
    // Array of strings to store descriptions
    String[] listDesc = new String[]{
            "Calculate Matting Requirements",
            "Get the Slope in Percent or Degrees",
            "Convert to/from Julain and Gregorian Calendars",
            "Calculate Roll Angle and Pole Height",
            "Review CESC and CGRI Checklists",
            "Random EAF Pictures",
            "About the EAF Toolkit"
    };
/*

            "Inspection checklist from HQMC website",
            "NAMP Computerized Self Examination Checklists",
            "Maintenance Codes",
            "CBR Calculator in the works",
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Each row in the list stores name, description and icon
        List<HashMap<String,String>> aList = new ArrayList<>();
        int lengthOfArray = listDesc.length;

        for(int i=0;i<lengthOfArray;i++){
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
                //LinearLayout linearLayoutParent = (LinearLayout) container;
                // Getting the inner Linear Layout
                //LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);
                // Getting the Country TextView
                //TextView tvCountry = (TextView) linearLayoutChild.getChildAt(0);

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
                    case 4: // Inspections
                        startActivity(new Intent(Main.this, Inspect.class));
                        break;
                    case 5: // Images
                        // TODO: Fix images to provide local data, Testing pics.class
//                        startActivity(new Intent(Main.this, Pics.class));
                        startActivity(new Intent(Main.this, ImageGridActivity.class));
                        break;
                    case 6: // About
                        startActivity(new Intent(Main.this, About.class));
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

        switch (id) {
            case R.id.action_matcalc: // Matting Calculator
                startActivity(new Intent(Main.this, MatCalc.class));
                break;
            case R.id.action_slope: // Slope
                startActivity(new Intent(Main.this, Slope.class));
                break;
            case R.id.action_julian: // Julian
                startActivity(new Intent(Main.this, Julian.class));
                break;
            case R.id.action_flols:  // FLOLS
                startActivity(new Intent(Main.this, FLOLS.class));
                break;
            case R.id.action_inspect: // Inspections
                startActivity(new Intent(Main.this, Inspect.class));
                break;
            case R.id.action_images: // Images
                startActivity(new Intent(Main.this, ImageGridActivity.class));
                break;
            case R.id.action_about: // About
                startActivity(new Intent(Main.this, About.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

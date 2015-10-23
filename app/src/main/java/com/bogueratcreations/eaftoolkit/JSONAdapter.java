package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jodyroth on 8/20/15.
 */
public class JSONAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return mJsonArray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Log.d("EAFtoolkit", "JSONadapter was called.");
        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.listview_inspect, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.InspectNameTextView = (TextView) convertView.findViewById(R.id.tvInspectName);
            holder.InspectDescTextView = (TextView) convertView.findViewById(R.id.tvInspectDesc);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        // More code after this

        // Get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

        // Grab the title and author from the JSON
        String inspectName = "";
        String inspectDesc = "";

        if (jsonObject.has("DisplayName")) {
            Log.d("EAFtoolkit","has DisplayName");
            inspectName = jsonObject.optString("DisplayName");
        } else {
            Log.d("EAFtoolkit", "didn't have DisplayName");
        }

        if (jsonObject.has("DisplayDesc")) {
            inspectDesc = jsonObject.optJSONArray("DisplayDesc").optString(0);
        }

        // Send these Strings to the TextViews for display
        holder.InspectNameTextView.setText(inspectName);
        holder.InspectDescTextView.setText(inspectDesc);

        return convertView;
    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView InspectNameTextView;
        public TextView InspectDescTextView;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
}

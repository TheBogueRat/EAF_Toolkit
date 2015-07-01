package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by TheBogueRat on 6/30/2015.
 * Borrowed from:
 * https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a-CursorAdapter
 */
public class ToolkitCursorAdapter extends CursorAdapter {
    public ToolkitCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_layout_csec, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        // TODO: Add the rest of the fields and customize the .xml
        TextView tvProgID = (TextView) view.findViewById(R.id.listProgID);
        TextView tvProgName = (TextView) view.findViewById(R.id.listProgName);
        // Extract properties from cursor
        String progID = cursor.getString(cursor.getColumnIndexOrThrow("programID"));
        int progName = cursor.getInt(cursor.getColumnIndexOrThrow("programName"));
        // Populate fields with extracted properties
        tvProgName.setText(progName);
        tvProgID.setText(String.valueOf(progID));
    }

}

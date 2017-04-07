package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewItemAdapter extends ArrayAdapter<ListViewItem> {

    public ListViewItemAdapter(Context context, List<ListViewItem> items) {
        super(context, R.layout.listview_main, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_main, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.listIcons);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.listTitles);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.listDesc);
            convertView.setTag(viewHolder);
        } else {
            // Recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Update the item view
        ListViewItem item = getItem(position);
        viewHolder.ivIcon.setImageDrawable(item.icon);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }
}


package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 10/24/16.
 */

public class projectListAdapter extends RealmBaseAdapter<Project> implements ListAdapter {

    private static class ViewHolder {
        TextView project;
    }

    public projectListAdapter(Context context, OrderedRealmCollection<Project> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.project = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project item = adapterData.get(position);
        viewHolder.project.setText(item.getProjName());
        return convertView;
    }
}

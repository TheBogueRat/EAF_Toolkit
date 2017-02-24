package com.bogueratcreations.eaftoolkit.DCP;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Project;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 10/24/16.
 *
 * Realm adapter for Projects
 */

public class PointsListAdapter extends RealmBaseAdapter<Point> implements ListAdapter {

    private static class ViewHolder {
        TextView pointNum;
    }

    public PointsListAdapter(Context context, OrderedRealmCollection<Point> realmResults) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.pointNum = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Point item = adapterData.get(position);
        viewHolder.pointNum.setText(item.getPointNum());
        return convertView;
    }
}

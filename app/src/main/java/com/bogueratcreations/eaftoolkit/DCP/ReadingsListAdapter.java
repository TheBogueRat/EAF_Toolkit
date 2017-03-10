package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Reading;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 2/27/17.
 *
 * Realm adapter for Projects
 */

public class ReadingsListAdapter extends RealmBaseAdapter<Reading> implements ListAdapter{

    private static class ViewHolder {
        TextView readingNum;
    }

    public ReadingsListAdapter(Context context, OrderedRealmCollection<Reading> realmResults) {
        super(context, realmResults);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.readingNum = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Reading item = adapterData.get(position);
        viewHolder.readingNum.setText(item.getReadingNum());
        return convertView;
    }
}

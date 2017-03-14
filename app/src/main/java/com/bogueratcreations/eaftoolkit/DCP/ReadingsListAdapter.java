package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.ListViewItem;
import com.bogueratcreations.eaftoolkit.R;

import java.util.Locale;

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
        TextView hammerType;
        TextView blows;
        TextView depth;
        TextView cbr;
        TextView total;
    }

    public ReadingsListAdapter(Context context, OrderedRealmCollection<Reading> realmResults) {
        super(context, realmResults);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_reading, parent, false);
            if (position%2 ==1) {
                convertView.setBackgroundColor(Color.LTGRAY);
            }
            viewHolder = new ViewHolder();
            viewHolder.readingNum = (TextView) convertView.findViewById(R.id.tv6);
            viewHolder.hammerType = (TextView) convertView.findViewById(R.id.tv5);
            viewHolder.blows = (TextView) convertView.findViewById(R.id.tv4);
            viewHolder.depth = (TextView) convertView.findViewById(R.id.tv3);
            viewHolder.cbr = (TextView) convertView.findViewById(R.id.tv2);
            viewHolder.total = (TextView) convertView.findViewById(R.id.tvTotal);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Reading item = adapterData.get(position);
        Log.d("Reading number: ", String.valueOf(item.getReadingNum()));
        viewHolder.readingNum.setText(String.valueOf(item.getReadingNum()));  // **** ERROR ****
        viewHolder.hammerType.setText(String.valueOf(item.getHammer()));
        viewHolder.blows.setText(String.valueOf(item.getBlows()));
        viewHolder.depth.setText(String.valueOf(item.getDepth()));
        String roundedCBR = String.format(Locale.US,"%.1f",item.getCbr());
        viewHolder.cbr.setText(roundedCBR);
        viewHolder.total.setText("undef");
        return convertView;
    }
}

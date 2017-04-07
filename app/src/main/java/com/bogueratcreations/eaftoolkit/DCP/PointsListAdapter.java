package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.R;

import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 10/24/16.
 *
 * Realm adapter for Projects
 */

public class PointsListAdapter extends RealmBaseAdapter<Point> implements ListAdapter {

    private static class ViewHolder {
        TextView point;
        TextView pointCbr;
    }

    public PointsListAdapter(Context context, OrderedRealmCollection<Point> realmResults) {
        super(context, realmResults);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_points, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.point = (TextView) convertView.findViewById(R.id.tvPoint);
            viewHolder.pointCbr = (TextView) convertView.findViewById(R.id.tvLowestCBR);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Point item = adapterData.get(position);
        viewHolder.point.setText(item.getPointNum()); // This should be the lowest recorded CBR which is set when saving the Readings
        String roundedCBR = String.format(Locale.US,"%.1f",item.getCbr());
        viewHolder.pointCbr.setText(roundedCBR);
        return convertView;
    }
}

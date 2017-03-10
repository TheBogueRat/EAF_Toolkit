package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 10/24/16.
 *
 * Realm adapter for Projects
 */

public class PointsListAdapter extends RealmBaseAdapter<Point> implements ListAdapter {

    private static class ViewHolder {
        TextView project;
        TextView projLoc;
        TextView projPoints;
    }

    public PointsListAdapter(Context context, OrderedRealmCollection<Point> realmResults) {
        super(context, realmResults);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_projects, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.project = (TextView) convertView.findViewById(R.id.tvProject);
            viewHolder.projLoc = (TextView) convertView.findViewById(R.id.tvProjectLoc);
            viewHolder.projPoints = (TextView) convertView.findViewById(R.id.tvPoints);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Point item = adapterData.get(position);
        viewHolder.project.setText(item.getPointNum());
        String soilType = "";
        switch (item.getSoilType()) {
            case 0:
                soilType = "Low Plasticity Clay";
                break;
            case 1:
                soilType = "High Plasticity Clay";
                break;
            case 2:
                soilType = "All Other Soils";
                break;
            default:
        }
        viewHolder.projLoc.setText(soilType);
        Double lowestCBR = item.getCbr(); // This should be the lowest recorded CBR which is set when saving the Readings
        viewHolder.projPoints.setText(lowestCBR.toString());
        return convertView;
    }
}

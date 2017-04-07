package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.DCP.model.Project;
import com.bogueratcreations.eaftoolkit.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jodyroth on 10/24/16.
 *
 * Realm adapter for Projects
 */

public class ProjectsListAdapter extends RealmBaseAdapter<Project> implements ListAdapter {

    private static class ViewHolder {
        TextView project;
        TextView projLoc;
        TextView projPoints;
    }

    public ProjectsListAdapter(Context context, OrderedRealmCollection<Project> realmResults) {
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
        Project item = adapterData.get(position);
        viewHolder.project.setText(item.getProjName());
        viewHolder.projLoc.setText(item.getProjLoc());
        Integer numberOfPoints = item.getPoints().size();
        viewHolder.projPoints.setText(numberOfPoints.toString());
        return convertView;
    }
}

package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jodyroth on 10/29/15.
 */
public class ProgramAdapter extends BaseAdapter {

    Context context;
    ArrayList<Program> listData;

    public ProgramAdapter(Context context,ArrayList<Program> listData){
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {

        return listData.size();
    }

    @Override
    public Object getItem(int position) {

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    class ViewHolder {

        private TextView tvProgram;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_program,null);
            viewHolder = new ViewHolder();
            viewHolder.tvProgram = (TextView) view.findViewById(R.id.tvProgram);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        // Getting list data for current position
        Program myProgram = listData.get(position);
        // Adding the question to the view
        String program = myProgram.getProgram();
        viewHolder.tvProgram.setText(program);
        // Set the color for the text in the list view item.
        viewHolder.tvProgram.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.BRCtext)));
        return view;
    }

}

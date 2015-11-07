package com.bogueratcreations.eaftoolkit;


import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jodyroth on 10/23/15.
 */
public class QuestionAdapter extends BaseAdapter {

    Context context;
    ArrayList<Question> listData;

    public QuestionAdapter(Context context,ArrayList<Question> listData){
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

        private TextView tvQuestionAndNum;
        private TextView tvReference;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_question,null);
            viewHolder = new ViewHolder();
            viewHolder.tvQuestionAndNum = (TextView) view.findViewById(R.id.tvQuestionAndNum);
            viewHolder.tvReference = (TextView) view.findViewById(R.id.tvReference);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        // Getting list data for current position
        Question myQuestion = listData.get(position);
        // Adding the question to the view
        String question = myQuestion.getQuestion();
        viewHolder.tvQuestionAndNum.setText(question);
        // Set text color
        viewHolder.tvQuestionAndNum.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.BRCtext)));
        // Adding the reference to the view
        String reference = myQuestion.getReference();
        viewHolder.tvReference.setText(reference);
        // Set the color for the text
        viewHolder.tvReference.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.BRCsubtext)));

        return view;
    }

}
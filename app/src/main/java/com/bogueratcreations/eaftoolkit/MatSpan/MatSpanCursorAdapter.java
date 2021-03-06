package com.bogueratcreations.eaftoolkit.MatSpan;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.R;

import java.text.DecimalFormat;

/**
 * Created by jodyroth on 7/4/16.
 */
public class MatSpanCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public MatSpanCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.listview_matspan, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        DecimalFormat df = new DecimalFormat("#,###");
        TextView tvName = (TextView) view.findViewById(R.id.tvMsName);
        String title = cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_NAME));
        tvName.setText(title);
        TextView tvWidth = (TextView) view.findViewById(R.id.tvMsWid);
        int width = cursor.getInt(cursor.getColumnIndex(MatSpanDbHelper.KEY_WIDTH));
        //tvWidth.setText(width);
        //TextView tvLength = (TextView) view.findViewById(R.id.tvMsLen);
        int length = cursor.getInt(cursor.getColumnIndex(MatSpanDbHelper.KEY_LENGTH));
        tvWidth.setText(df.format(width) + " x " + df.format(length));
        //tvLength.setText(length);
        TextView tvSpans = (TextView) view.findViewById(R.id.tvProject);
        String spans = cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_SPANS));
        tvSpans.setText("(" + spans + ")");
        TextView tvArea = (TextView) view.findViewById(R.id.tvMsArea);
        int intArea = width * length * Integer.parseInt(spans);
        String strArea = df.format(intArea);
        tvArea.setText(strArea);
        final CheckBox chkSelected = (CheckBox) view.findViewById(R.id.checkBoxMatSpan);
        String selected = cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_SELECTED));
        chkSelected.setTag(cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_ID)));
        final boolean isSelected = (Integer.parseInt(selected) == 1);
        chkSelected.setChecked(isSelected);
        chkSelected.setSelected(isSelected);
//        chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                //MatSpanDbAdapter tempAdapter = new MatSpanDbAdapter(context);
//                //int tag = Integer.parseInt(chkSelected.getTag().toString());
//                //MatSpan.updateCheckedValue(tag, isSelected?1:0);
//            }
//        });
        TextView tvLay = (TextView) view.findViewById(R.id.tvMsLay);
        String layPat = cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_lAYPAT));
        switch (layPat) {
            case "0":
                tvLay.setText(" B/W ");
                break;
            case "1":
                tvLay.setText(" 2-1 ");
                break;
            case "2":
            case "3":
                tvLay.setText(" 3-4 ");
                break;
            default:
                tvLay.setText(" Unk ");
                break;
        }
        //String starter = cursor.getString(cursor.getColumnIndex(MatSpanDbHelper.KEY_START));
        int starter = cursor.getInt(cursor.getColumnIndex(MatSpanDbHelper.KEY_START));
        MatSpanModel matSpan = new MatSpanModel(title, width, length,
                Integer.parseInt(spans),
                Integer.parseInt(layPat), starter);
        matSpan.calcMat();
        TextView tvPkgs = (TextView) view.findViewById(R.id.tvMsPkg);
        tvPkgs.setText(df.format(matSpan.getF71()) + "/" + df.format(matSpan.getF72()));
    }

}

package com.bogueratcreations.eaftoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jodyroth on 6/30/16.
 */
public class MatSpanDbAdapter {

    SQLiteDatabase db;
    MatSpanDbHelper helper;
    Context context;

    public MatSpanDbAdapter(Context c) {
        context = c;
    }

    public MatSpanDbAdapter openToRead() {
        helper = new MatSpanDbHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    public MatSpanDbAdapter openToWrite() {
        helper = new MatSpanDbHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    public void Close() {
        db.close();
    }

    public long insertMatSpan(String name, int wid, int len, int spans, int layPat, int start, int selected) {
        ContentValues contentValues = new ContentValues();
        if (TextUtils.isEmpty(name)) {name = "No time for a name";}
        contentValues.put(MatSpanDbHelper.KEY_NAME, name);
        contentValues.put(MatSpanDbHelper.KEY_WIDTH, wid);
        contentValues.put(MatSpanDbHelper.KEY_LENGTH, len);
        contentValues.put(MatSpanDbHelper.KEY_SPANS, spans);
        contentValues.put(MatSpanDbHelper.KEY_lAYPAT, layPat);
        contentValues.put(MatSpanDbHelper.KEY_START, start);
        contentValues.put(MatSpanDbHelper.KEY_SELECTED, selected);
        openToWrite();
        long val = db.insert(MatSpanDbHelper.TABLE_NAME, null,
                contentValues);
        Close();
        return val;
    }

    public Cursor queryAll() {
        String[] cols = {MatSpanDbHelper.KEY_ID, MatSpanDbHelper.KEY_NAME, MatSpanDbHelper.KEY_WIDTH, MatSpanDbHelper.KEY_LENGTH, MatSpanDbHelper.KEY_SPANS, MatSpanDbHelper.KEY_lAYPAT, MatSpanDbHelper.KEY_START, MatSpanDbHelper.KEY_SELECTED};
        openToWrite();
        Cursor c = db.query(MatSpanDbHelper.TABLE_NAME, cols, null, null, null, null, MatSpanDbHelper.KEY_NAME);
        return c;
    }

    public Cursor querySpan(int spanId) {
        String[] cols = {MatSpanDbHelper.KEY_ID, MatSpanDbHelper.KEY_NAME, MatSpanDbHelper.KEY_WIDTH, MatSpanDbHelper.KEY_LENGTH, MatSpanDbHelper.KEY_SPANS, MatSpanDbHelper.KEY_lAYPAT, MatSpanDbHelper.KEY_START, MatSpanDbHelper.KEY_SELECTED};
        openToWrite();
        Cursor c = db.query(MatSpanDbHelper.TABLE_NAME, cols, MatSpanDbHelper.KEY_ID + "=" + spanId, null, null, null, null);
        return c;
    }
// TODO: Verify functionality
    public Cursor querySelected() {
        String[] cols = {MatSpanDbHelper.KEY_ID, MatSpanDbHelper.KEY_NAME, MatSpanDbHelper.KEY_WIDTH, MatSpanDbHelper.KEY_LENGTH, MatSpanDbHelper.KEY_SPANS, MatSpanDbHelper.KEY_lAYPAT, MatSpanDbHelper.KEY_START, MatSpanDbHelper.KEY_SELECTED};
        openToRead();
        Cursor c = db.query(MatSpanDbHelper.TABLE_NAME, cols, MatSpanDbHelper.KEY_SELECTED + "= 1", null, null, null, MatSpanDbHelper.KEY_NAME);
        return c;
    }

    public String summarizeSelected() {
        String summaryOfSelectedSpans = "";
        String spanSummary = "";
        int i12 = 0, i6 = 0, iF71 = 0, iF72=0, iF44=0;
        try {
            Cursor cursor = querySelected();
            if(!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    MatSpanModel matSpan = new MatSpanModel();
                    matSpan.setName(cursor.getString(1));
                    matSpan.setWid(cursor.getInt(2));
                    matSpan.setLen(cursor.getInt(3));
                    matSpan.setQty(cursor.getInt(4));
                    matSpan.setLay(cursor.getInt(5));
                    matSpan.setStart(cursor.getInt(6));
                    matSpan.calcMat();
                    spanSummary += matSpan.summarize() + "\n - .... . / -... --- --. ..- . / .-. .- - \n\n";
                    i12 += matSpan.getSheet12();
                    i6 += matSpan.getSheet6();
                    iF71 += matSpan.getF71();
                    iF72 += matSpan.getF72();
                    iF44 += matSpan.getF44();
                }
            }
        } catch (Exception e){
            Log.e("EAF Toolkit", e + "");
        }
        // Create model and send values for quick summary of all spans combined;
        MatSpanModel matSpan = new MatSpanModel();
        matSpan.setSheet12(i12);
        matSpan.setSheet6(i6);
        matSpan.setF71(iF71);
        matSpan.setF72(iF72);
        matSpan.setF44(iF44);
        summaryOfSelectedSpans = "\n" + matSpan.summarizeMultiple() + spanSummary;

        return summaryOfSelectedSpans;
    }
    // Fill an array of MatSpanModel objects from the database of selected spans
    public ArrayList<MatSpanModel> getSelectedSpans() {
        // Gets the questions for the selected program in the assigned table.
        String summaryOfSpans;
        openToRead(); // sets db and helper variables
        ArrayList<MatSpanModel> matSpanList = null;
        try{
            matSpanList = new ArrayList<MatSpanModel>();
            Cursor cursor = querySelected();
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    MatSpanModel matSpan = new MatSpanModel();
                    matSpan.setName(cursor.getString(1));
                    matSpan.setWid(cursor.getInt(2));
                    matSpan.setLen(cursor.getInt(3));
                    matSpan.setQty(cursor.getInt(4));
                    matSpan.setLay(cursor.getInt(5));
                    matSpan.setStart(cursor.getInt(6));
                    matSpan.calcMat();
                    String me = matSpan.summarize();
                    matSpanList.add(matSpan);
                }
            }
            Close();
        }catch (Exception e){
            Log.e("EAF Toolkit", e + "");
        }
        return matSpanList;
    }

    public long countSelected() {
        return DatabaseUtils.queryNumEntries(db, MatSpanDbHelper.TABLE_NAME, MatSpanDbHelper.KEY_SELECTED + "= 1");
    }

    public long updateSpan(int rowId, String name, int wid, int len, int spans, int layPat, int start, int selected) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MatSpanDbHelper.KEY_NAME, name);
        contentValues.put(MatSpanDbHelper.KEY_WIDTH, wid);
        contentValues.put(MatSpanDbHelper.KEY_LENGTH, len);
        contentValues.put(MatSpanDbHelper.KEY_SPANS, spans);
        contentValues.put(MatSpanDbHelper.KEY_lAYPAT, layPat);
        contentValues.put(MatSpanDbHelper.KEY_START, start);
        contentValues.put(MatSpanDbHelper.KEY_SELECTED, selected);

        openToWrite();
        long val = db.update(MatSpanDbHelper.TABLE_NAME, contentValues, MatSpanDbHelper.KEY_ID + "=" + rowId, null);
        Close();
        return val;
    }

    public int updateSpanStatus(int rowId, int selected) {
        int rowsAffected = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MatSpanDbHelper.KEY_SELECTED, selected);
        openToWrite();
        try {
            rowsAffected = db.update(MatSpanDbHelper.TABLE_NAME, contentValues, MatSpanDbHelper.KEY_ID + "=" + rowId, null);
        } catch (SQLiteDiskIOException sqlIOE) {
            Log.d("EAFtoolkit Error: ","SQLiteDiskIOException was thrown in updateSpanStatus...");
        }
        Close();
        return rowsAffected;
    }

    public int deleteRecord(int rowId) {
        openToWrite();
        int val = db.delete(MatSpanDbHelper.TABLE_NAME, MatSpanDbHelper.KEY_ID + "=" + rowId, null);
        Close();
        return val;
    }
}

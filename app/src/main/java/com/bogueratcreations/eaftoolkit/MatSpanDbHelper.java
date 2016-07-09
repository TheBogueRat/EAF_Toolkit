package com.bogueratcreations.eaftoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MatSpanDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "EAFToolkitMatting.db";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "_name";
    public static final String KEY_LENGTH = "_len";
    public static final String KEY_WIDTH = "_wid";
    public static final String KEY_SPANS = "_spans";
    public static final String KEY_lAYPAT = "_layPat";
    public static final String KEY_START = "_start";
    public static final String KEY_SELECTED = "_selected";

    public static final String TABLE_NAME = "MatSpan";

    private Boolean oldData = false;

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME + " STRING," +
            KEY_LENGTH + " INTEGER," +
            KEY_WIDTH + " INTEGER," +
            KEY_SPANS + " INTEGER," +
            KEY_lAYPAT + " INTEGER," +
            KEY_START + " INTEGER," +
            KEY_SELECTED + " INTEGER)";  //0-false 1-true

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String SELECT_DATA =
            KEY_NAME + ", " +
            KEY_WIDTH + ", " +
            KEY_LENGTH + ", " +
            KEY_SPANS + ", " +
            KEY_lAYPAT + ", " +
            KEY_START + ", " +
            KEY_SELECTED;

    public MatSpanDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        if (BuildConfig.DEBUG && !oldData) {
            // Add test records if we don't have old data and are running in DEBUG mode.
            seedDbDebug(db);
        } else if (!oldData) {
            // Add sample record if there was no old data
            seedDb(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //code to keep table data
        List<MatSpanModel> objList = new ArrayList<MatSpanModel>();

        String selectQuery = "SELECT " + SELECT_DATA + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MatSpanModel o = new MatSpanModel();
                o.setName(cursor.getString(0));
                o.setWid(cursor.getInt(1));
                o.setLen(cursor.getInt(2));
                o.setQty(cursor.getInt(3));
                o.setLay(cursor.getInt(4));
                o.setStart(cursor.getInt(5));
                o.setSelected(cursor.getInt(6));
                objList.add(o);
            } while (cursor.moveToNext());
        }
        if (cursor.getCount() > 0) {
            // Mark as having data so we don't create new seed data
            oldData = true;
        }
        cursor.close();

        //done storing data, now upgrade DB
        db.execSQL(DROP_TABLE);
        onCreate(db);

        //now insert our saved table data
        for (MatSpanModel obj_MSM: objList){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, obj_MSM.getName());
            values.put(KEY_WIDTH, obj_MSM.getWid());
            values.put(KEY_LENGTH, obj_MSM.getLen());
            values.put(KEY_SPANS, obj_MSM.getQty());
            values.put(KEY_lAYPAT, obj_MSM.getLay());
            values.put(KEY_START, obj_MSM.getStart());
            values.put(KEY_SELECTED, obj_MSM.isSelected());
            db.insert(TABLE_NAME, null, values);
        }
    }

    private void seedDbDebug(SQLiteDatabase db) {
        // This function allows me to evaluate test data in debug mode
        String SEED_TABLE1 = "INSERT INTO " + TABLE_NAME + "(" +
                SELECT_DATA + ") VALUES (\"Debug Span 1\",96,96,2,0,0,0);";

        String SEED_TABLE2 = "INSERT INTO " + TABLE_NAME + "(" +
                SELECT_DATA + ") VALUES (\"Debug Span 2\",150,4020,1,2,1,1);";

        db.execSQL(SEED_TABLE1);
        db.execSQL(SEED_TABLE2);
    }

    private void seedDb(SQLiteDatabase db) {
        // This function allows me to evaluate test data in debug mode
        String SEED_TABLE = "INSERT INTO " + TABLE_NAME + "(" +
                SELECT_DATA + ") VALUES (\"Example Span\",96,96,2,0,0,0);";
        db.execSQL(SEED_TABLE);
    }


    // TODO: Remove for production - This is for the Android Database Manager
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}


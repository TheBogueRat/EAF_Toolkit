package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TheBogueRat on 6/10/2015.
 */
public class CSECdb extends SQLiteOpenHelper {
    //The Android's default system path of your application database.
    private static String DB_PATH = "data/data/com.bogueratcreations.eaftoolkit.app/databases/";
    private static String DB_NAME = "eafCSEC";
    private static String TABLE_LOCATION = "CSEC";

    private final Context context;
    private SQLiteDatabase db;


    // constructor
    public CSECdb(Context context) {

        super( context , DB_NAME , null , 1);
        this.context = context;

    }


    // Creates a empty database on the system and rewrites it with your own database.
    public void create() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    // Check if the database exist to avoid re-copy the data
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{


            String path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            // database don't exist yet.
            e.printStackTrace();

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    // copy your assets db to the new system DB
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    //Open the database
    public boolean open() {

        try {
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            return true;

        } catch(SQLException sqle) {
            db = null;
            return false;
        }

    }

    @Override
    public synchronized void close() {

        if(db != null)
            db.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //
    // PUBLIC METHODS TO ACCESS DB CONTENT
    //


    // Get locations
    public List<CSECquestions> getLocations() {

        List<CSECquestions> locations = null;

        try {

            String query = "SELECT * FROM " + TABLE_LOCATION;
            SQLiteDatabase db = SQLiteDatabase.openDatabase( DB_PATH + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            // go over each row, build elements and add it to list
            locations = new LinkedList<CSECquestions>();

            if (cursor.moveToFirst()) {
                do {
                    CSECquestions location  = new CSECquestions();
                    location.id      = Integer.parseInt(cursor.getString(0));
                    location.ProgramID = cursor.getString(1);
                    location.ProgramName = cursor.getString(2);
                    location.Qnum = cursor.getString(3);
                    location.Question = cursor.getString(4);
                    location.Reference = cursor.getString(5);
                    location.Complies = Integer.parseInt(cursor.getString(6));
                    location.UserMod = Integer.parseInt(cursor.getString(7));
                    locations.add(location);

                } while (cursor.moveToNext());
            }
        } catch(Exception e) {
            // sql error
        }

        return locations;
    }
}

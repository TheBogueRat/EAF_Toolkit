package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by jodyroth on 6/29/15.
 */
public class CGRIDbHelper extends SQLiteAssetHelper {

    // The Android default sys path for app data
    private static String DB_PATH = "/data/data/com.bogueratcreations.eaftoolkit/databases";
    private static String DB_NAME = "csec.db";
    private SQLiteDatabase csecDatabase;
    private final Context myContext;

    // Constructor
    // Takes and keeps reference of the passed context to access the app assets and resources
    public CGRIDbHelper(Context context) {
        // context, db name, null, database_version
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    // Creates an empty db on device and rewrites it with included db
    public void createDataBase() throws IOException {
        boolean dbExists = checkDataBase();
        if (dbExists) {
            //do nothing
        } else {
            // Create empty db for initial data transfer
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e) {
            // db doesn't exist
        }
        if(checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    // Copies the database
    private void copyDataBase() throws IOException {
        // Open local db
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to new db
        String outFileName = DB_NAME + DB_NAME;
        // Open empty db as output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        // Open the db
        String myPath = DB_PATH + DB_NAME;
        csecDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (csecDatabase != null) {
            csecDatabase.close();
        }
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*
// Started working from www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications

// Create an instance of myDBHelper Class

DataBaseHelper myDbHelper = new DataBaseHelper();
myDbHelper = new DataBaseHelper(this);
try{myDbHelper.createDataBase();}
catch{IOException ioe) {throw new Error("Unable to create db"); }
try{myDbHelper.openDataBase(); } catch(SQLException sqle){ throw sqle; }


    */
}



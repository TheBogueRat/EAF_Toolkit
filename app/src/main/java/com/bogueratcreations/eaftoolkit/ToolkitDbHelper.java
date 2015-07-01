package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by TheBogueRat on 6/30/2015.
 */
public class ToolkitDbHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "eafToolkit.sqlite";
    private static final String DB_PATH = "/data/data/com.bogueratcreations.eafToolkit/databases";
    private static final int DB_VER = 1;
    private final Context myContext;

    public ToolkitDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        this.myContext = context;
    }

/*    @Override
    public void onCreate(SQLiteDatabase db) {
        // Copy the database over
    }*/

    // Creates an empty db on device and rewrites it with included db
    public void createDataBase() throws IOException {
        boolean dbExists = checkDataBase();
        if (!dbExists) {
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
        return checkDB != null;
    }

    // Copies the database
    private void copyDataBase() throws IOException {
        // Open local db
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to new db
        String outFileName = DB_PATH + DB_NAME;
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

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database????
    }
}

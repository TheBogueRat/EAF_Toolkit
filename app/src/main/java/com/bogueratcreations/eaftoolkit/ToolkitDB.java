package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by jodyroth on 6/30/15.
 * Helper class to manage the database
 */
public class ToolkitDB extends SQLiteAssetHelper {

    private static final String DB_PATH = "/data/data/com.bogueratcreations.eafToolkit/databases";
    private static final String DB_NAME = "eafToolkit.sqlite";
    private static final int DB_VER = 1;
    private SQLiteDatabase eafToolkitDatabase;
    private final Context myContext;

    public interface TABLES {
        String CSEC = "csec";
        String CGRI_EAF = "cgri_eaf";
        String CGRI_ARFF = "cgri_arff";
        String NAMP = "namp";
    }

    public interface questColumns {
        String PROG = "programID";
        String NAME = "programName";
        String NUM = "number";
        String QUEST = "question";
        String REF = "reference";
        String COMP = "complies";
        String MOD = "userMod";
        String REM = "remarks";
    }

    public ToolkitDB(Context context) {
        super(context, DB_NAME, null, DB_VER);
        this.myContext = context;

    }

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

    public void openDataBase() throws SQLException {
        // Open the db
        String myPath = DB_PATH + DB_NAME;
        eafToolkitDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (eafToolkitDatabase != null) {
            eafToolkitDatabase.close();
        }
        super.close();
    }

    // TODO: Pass Table Name as Parameter
    public Cursor getQuestions() {

        // Table name that will be passed as argument
        String TABLE_NAME = "csec";

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _ID","programID","programName","number","question","reference","complies","userMod","remarks"};
        String sqlTables = TABLE_NAME;

        qb.setTables(sqlTables);
        // add where statement later to pull specific sections
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public int getUpgradeVersion() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"MAX (version)"};
        String sqlTables = "upgrades";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        int v = 0;
        c.moveToFirst();
        if (!c.isAfterLast()) { v = c.getInt(0);}
        c.close();
        return v;

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

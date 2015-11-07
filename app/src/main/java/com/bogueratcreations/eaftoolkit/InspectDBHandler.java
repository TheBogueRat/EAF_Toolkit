package com.bogueratcreations.eaftoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jodyroth on 10/23/15.
 */
public class InspectDBHandler extends SQLiteOpenHelper implements InspectListener{
    // TODO: Make this agnostic to any list so we can query down to the program from CSEC & CGRI
    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "EAFToolkit.db";
    private static final String KEY_ID = "_id";
    private static final String KEY_PROGRAM = "_program";
    private static final String KEY_QUESTION = "_question";
    private static final String KEY_REFERENCE = "_reference";

    private String TABLE_NAME = "CSEC";

    String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_PROGRAM+" TEXT,"+KEY_QUESTION+" TEXT,"+KEY_REFERENCE+" TEXT)";
    String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public InspectDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CSEC ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_PROGRAM+" TEXT,"+KEY_QUESTION+" TEXT,"+KEY_REFERENCE+" TEXT)");
        db.execSQL("CREATE TABLE EAF ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_PROGRAM+" TEXT,"+KEY_QUESTION+" TEXT,"+KEY_REFERENCE+" TEXT)");
        db.execSQL("CREATE TABLE ARFF ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_PROGRAM+" TEXT,"+KEY_QUESTION+" TEXT,"+KEY_REFERENCE+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CSEC");
        db.execSQL("DROP TABLE IF EXISTS EAF");
        db.execSQL("DROP TABLE IF EXISTS ARFF");
        onCreate(db);
    }

    @Override
    public void setTableName(String tableName) {
        // Need to set the table name before using this object.
        TABLE_NAME = tableName;
    }

    @Override
    public void addQuestion(Question question) {
        // Adds a question to the assigned table.
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_PROGRAM, question.getProgram());
            values.put(KEY_QUESTION, question.getQuestion());
            values.put(KEY_REFERENCE,question.getReference());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem", e + "");
        }
    }

    // Fill an array from the database
    @Override
    public ArrayList<Question> getAllQuestion() {
        // Gets all questions from the assigned table
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Question> questionList = null;
        try{
            questionList = new ArrayList<Question>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Question question = new Question();
                    question.setId(cursor.getInt(0));
                    question.setProgram(cursor.getString(1));
                    question.setQuestion(cursor.getString(2));
                    question.setReference(cursor.getString(3));
                    questionList.add(question);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return questionList;
    }

    // Fill an array from the database for a specific program
    @Override
    public ArrayList<Question> getQuestions(String program) {
        // Gets the questions for the selected program in the assigned table.
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Question> questionList = null;
        try{
            questionList = new ArrayList<Question>();
            // TODO: filter by program here?
            String QUERY = "SELECT * FROM "+TABLE_NAME + " WHERE "+ KEY_PROGRAM + "='" + program + "'";
            Cursor cursor = db.rawQuery(QUERY, null);
            Log.i("EAF Toolkit", "Received program: " + program);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Question question = new Question();
                    question.setId(cursor.getInt(0));
                    // is setting the program needed?  Can save time here.
                    question.setProgram(cursor.getString(1));
                    question.setQuestion(cursor.getString(2));
                    question.setReference(cursor.getString(3));
                    questionList.add(question);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return questionList;
    }

    // Fill an array from the database for a specific program
    @Override
    public ArrayList<Program> getPrograms() {
        // Returns a list of programs in the assigned table.
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Program> programList = null;
        try{
            programList = new ArrayList<Program>();
            // TODO: filter by program here?
            String QUERY = "SELECT DISTINCT " + KEY_PROGRAM + " FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            Log.i("EAF Toolkit", "Selected Distinct Data");
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Program program = new Program();
                    program.setProgram(cursor.getString(0));
                    programList.add(program);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }

        Log.i("EAF Toolkit", "Returning the list");
        return programList;
    }

    @Override
    public int getQuestionCount() {
        // Returns the number of questions in the assigned table.
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return 0;
    }
}

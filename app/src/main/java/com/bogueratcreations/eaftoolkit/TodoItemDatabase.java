package com.bogueratcreations.eaftoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheBogueRat on 6/30/2015.
 */
public class TodoItemDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "eafToolkit.sqlite";

    // Todo table name
    private static final String TABLE_CSEC = "csec";

    // Todo Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_PROG_ID = "programID";
    private static final String KEY_PROG_NAME = "priority";

    public TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating our initial tables
    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Construct a table for todo items
        // TODO: try to use the copy method here
        String CREATE_CSEC_TABLE = "CREATE TABLE " + TABLE_CSEC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROG_ID + " INTEGER,"
                + KEY_PROG_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CSEC_TABLE);
    }

    // Upgrading the database between versions
    // This method is called when database is upgraded like modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CSEC);
            // Create tables again
            onCreate(db);
        }
    }

    // Insert record into the database
    public void addTodoItem(TodoItem item) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(KEY_PROG_ID, item.getBody());
        values.put(KEY_PROG_NAME, item.getPriority());
        // Insert Row
        db.insertOrThrow(TABLE_CSEC, null, values);
        db.close(); // Closing database connection
    }

    // Returns a single todo item by id
    public TodoItem getTodoItem(int id) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_CSEC,  // TABLE
                new String[] { KEY_ID, KEY_PROG_ID, KEY_PROG_NAME }, // SELECT
                KEY_ID + "= ?", new String[] { String.valueOf(id) },  // WHERE, ARGS
                null, null, "id ASC", "100"); // GROUP BY, HAVING, ORDER BY, LIMIT
        if (cursor != null)
            cursor.moveToFirst();
        // Load result into model object
        TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        // Close the cursor
        if (cursor != null)
            cursor.close();
        // return todo item
        return item;
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CSEC;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
                item.setId(cursor.getInt(0));
                // Adding todo item to list
                todoItems.add(item);
            } while (cursor.moveToNext());
        }

        // Close the cursor
        if (cursor != null)
            cursor.close();

        // return todo list
        return todoItems;
    }

    public int getTodoItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CSEC;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // Close the cursor
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateTodoItem(TodoItem item) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Setup fields to update
        ContentValues values = new ContentValues();
        values.put(KEY_PROG_ID, item.getBody());
        values.put(KEY_PROG_NAME, item.getPriority());
        // Updating row
        int result = db.update(TABLE_CSEC, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
        return result;
    }

    public void deleteTodoItem(TodoItem item) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(TABLE_CSEC, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        // Close the database
        db.close();
    }

}

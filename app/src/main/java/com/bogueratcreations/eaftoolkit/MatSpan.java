package com.bogueratcreations.eaftoolkit;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.R;

public class MatSpan extends AppCompatActivity {
    MatSpanCursorAdapter customAdapter;
    MatSpanDbAdapter adapter;
    MatSpanDbHelper helper;
    SQLiteDatabase db;
    ListView spanList;
    Button summaryBtn;
    Cursor cursor;
    Long selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matspan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MatSpan.this,
                        MatSpanNewActivity.class);
                startActivity(registerIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        spanList = (ListView) findViewById(R.id.lv_MatSpan);
        adapter = new MatSpanDbAdapter(this);
        cursor = adapter.queryAll();
        // Here we query database
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new MatSpanCursorAdapter(MatSpan.this, cursor, 0);
                spanList.setAdapter(customAdapter);
            }
        });
        registerForContextMenu(spanList);
        summaryBtn = (Button)findViewById(R.id.btnSummary);
        // Show or hide the Summary button
        selectedItems = adapter.countSelected();
        if (selectedItems > 0) {
            summaryBtn.setVisibility(View.VISIBLE);
        } else {
            summaryBtn.setVisibility(View.GONE);
        }

        spanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Bundle passdata = new Bundle();
                Cursor listCursor = (Cursor) adapterView.getItemAtPosition(position);
                int spanId = listCursor.getInt(listCursor.getColumnIndex(MatSpanDbHelper.KEY_ID));
                passdata.putInt("keyid", spanId);
                Intent passIntent = new Intent(MatSpan.this, MatSpanEditActivity.class);
                passIntent.putExtras(passdata);
                startActivity(passIntent);
            }
        });
        spanList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                spanList.showContextMenuForChild(view);
                return true;
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mat_span_delete, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(this,"update",Toast.LENGTH_LONG).show();
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                // Remove entry based on - info.id;
                adapter.deleteRecord((int)info.id);
                cursor = adapter.queryAll();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        customAdapter.changeCursor(cursor);
                        spanList.setAdapter(customAdapter);
                    }
                });
                return true;
            case R.id.action_edit:
                // move to edit screen passing in the currently selected record.
                AdapterView.AdapterContextMenuInfo info2 =
                        (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Bundle passdata = new Bundle();
                passdata.putInt("keyid", (int)info2.id);
                Intent passIntent = new Intent(MatSpan.this,
                        MatSpanEditActivity.class);
                passIntent.putExtras(passdata);
                startActivity(passIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickHandler(View view) {
        if(view.getId() == R.id.checkBoxMatSpan){
            // TODO: Setup in MatSpanCursorAdapter on set's initial state, need to assign selected/checked values again to implement toggle.
            // selected status is before the change so if it comes in as true, we need to set it to false
            boolean selected = !view.isSelected();
            int dbRowId = Integer.parseInt(view.getTag().toString());
            adapter.updateSpanStatus(dbRowId, selected?1:0);
            view.setSelected(selected);
            selectedItems += (selected)?1:-1;
            // Show or hide the Summary button
            if (selectedItems > 0) {
                summaryBtn.setVisibility(View.VISIBLE);
            } else {
                summaryBtn.setVisibility(View.GONE);
            }
        } else if (view.getId() == R.id.btnSummary) {
            Intent summary = new Intent(MatSpan.this, MatSpanSummary.class);
            startActivity(summary);
        }
    }
}

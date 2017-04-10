package com.bogueratcreations.eaftoolkit.DCP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.DCP.model.Point;
import com.bogueratcreations.eaftoolkit.DCP.model.Reading;
import com.bogueratcreations.eaftoolkit.R;

import java.util.Locale;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class Readings extends AppCompatActivity {

    // TODO: Toggle graph plot style
    // TODO: Add graphing for current
    // TODO: Add total depth helper to show the total when selecting a new depth.

    long passedPointID;
    Point passedPoint;
    private Realm realm;

    TextView tvPointInfo;
    NumberPicker npHammer;
    NumberPicker npBlows;
    NumberPicker npDepth;
    TextView tvDepthTotal;

    Button btnAppend;

    int clickedPos = -1;      // Row to insert above
    int longClickedPos = -1;  // Row to be replaced

    int totalDepth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        realm = Realm.getDefaultInstance();

        // Get the point name (ID) for use in finding associated points and for use in creating new points.
        passedPointID = getIntent().getLongExtra("pointId",-1);
        // TODO: If receiving -1, I wasn't passed a valid point...

        tvPointInfo = (TextView) findViewById(R.id.tvPointInfo);
        tvDepthTotal = (TextView) findViewById(R.id.tvDepthPicker);

        npHammer = (NumberPicker) findViewById(R.id.pickHammer);
        npBlows = (NumberPicker) findViewById(R.id.pickBlows);
        npDepth = (NumberPicker) findViewById(R.id.pickDepth);


        final String[] hammers = {"17.6", "10.1"};
        npHammer.setDisplayedValues(hammers);
        npHammer.setMinValue(0);
        npHammer.setMaxValue(hammers.length - 1);
        npHammer.setWrapSelectorWheel(true);

        npBlows.setMinValue(0);
        npBlows.setMaxValue(50);

        npDepth.setMinValue(0);
        npDepth.setMaxValue(30);
        String[] depthValues = new String[31];
        for (int i = 0; i < depthValues.length; i++) {
            String number = Integer.toString(i*5);
            depthValues[i] = number;
        }
        npDepth.setDisplayedValues(depthValues);
//        npDepth.setFormatter(new NumberPicker.Formatter() {
//            @Override
//            public String format(int value) {
//                Log.d("EAFToolkit", "Depth Picker index value: " + value);
//                int temp = value * 5;
//                return "" + temp;
//            }
//        });
        npDepth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tvDepthTotal.setText("Depth: " + (totalDepth + (newVal * 5)));
            }
        });

        passedPoint = realm.where(Point.class)
                .equalTo("id", passedPointID)
                .findFirst();
        tvPointInfo.setText("Point: " + passedPoint.getPointNum());

        final RealmResults<Reading> readings = realm.where(Reading.class)
                .equalTo("point.id", passedPointID)
                .findAllSorted("readingNum");
        final ReadingsRealmAdapter readingsRealmAdapter = new ReadingsRealmAdapter(this, readings, true, true);
        final RealmRecyclerView realmRecyclerView = (RealmRecyclerView) findViewById(R.id.listViewReadings);
        realmRecyclerView.setAdapter(readingsRealmAdapter);
        realmRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isPressed()) {
                    view.setPressed(false);
                } else {
                    view.setPressed(true);
                }
            }
        });

        if (readings.size() > 0) {
            // Promote selection of minimum depth
            npDepth.setValue(5);
            npBlows.setValue(1);
            // set totalDepth to existing set
            totalDepth = readings.get(readings.size() - 1).getTotalDepth();
        }
        // Set total depth helper value on screen.
        tvDepthTotal.setText("Depth: " + (totalDepth + (npDepth.getValue() * 5)));

        btnAppend = (Button) findViewById(R.id.btnReading);
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int readingsSize = passedPoint.getReadings().size();
                if ((npDepth.getValue() < 5) && (readingsSize > 0)){
                    if ((longClickedPos != 0) && (clickedPos != 0)) {
                        // Only valid for the first reading if not editing the first one.
                        Toast.makeText(Readings.this, "After the first reading, depth must be 25 or more.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if ((readingsSize > 0) && (npBlows.getValue() == 0) && (longClickedPos != 0)){
                    // Prevent zero blows from being entered unless on the initial reading
                    Toast.makeText(Readings.this, "After the first reading, blows must be 1 or more.", Toast.LENGTH_LONG).show();
                    return;
                }
                final Reading newReading = new Reading();
                newReading.setId(PrimaryKeyFactory.getInstance().nextKey(Reading.class));
                newReading.setHammer(npHammer.getValue() + 1);
                if (readingsSize > 0) {
                    newReading.setBlows(npBlows.getValue());
                } else {
                    newReading.setBlows(0);
                }
                newReading.setDepth(npDepth.getValue() * 5);
                newReading.setPoint(passedPoint);
                newReading.setSoilType(passedPoint.getSoilType());
                newReading.calcCbr();

                if (longClickedPos == -1) {
                    if (clickedPos > -1) {
                        // New reading will be this index, later we'll reindex the others before adding to realm in the transaction
                        newReading.setReadingNum(clickedPos);  // Need to increment in transaction...
                    } else {
                        // Appending item to bottom of the list.
                        newReading.setReadingNum(readingsSize); // size is next number in zero-based array.
                    }
                } else {
                    // Reorder readings to insert the new one and set the newReading.Num
                    int index = 0;
                    for (Reading r : readings) {
                        if (index == longClickedPos) {
                            // Update this reading by assigning it the same ID for the copytorealmorupdate...
                            newReading.setId(r.getId());
                        }
                        index++;
                    }
                    // Set new readingNum to location of insert.
                    newReading.setReadingNum(longClickedPos);
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //realm.beginTransaction();
                        if (clickedPos > -1) {
                            // Reorder readings to insert the new one and set the newReading.Num
                            int index = 0;
                            for (Reading r : readings) {
                                if (index >= clickedPos) {
                                    readings.get(index).setReadingNum(index + 1);
                                }
                                index++;
                            }
                        }
                        realm.copyToRealmOrUpdate(newReading);

                        // recalculate total depths and get lowest CBR
                        if (longClickedPos == -1) {
                            // Not replacing an existing reading
                            passedPoint.getReadings().add(newReading);
                        }
                        // Recalculate the total depths and update the lowestCBR.
                        int i = 0;
                        int depth = 0;
                        double lowestCBR = 100; // The lowest CBR recorded that is not 0
                        for (Reading r : passedPoint.getReadings().sort("readingNum")) {
                            // Update Depth
                            depth = depth + r.getDepth();
                            r.setTotalDepth(depth);
                            // Update lowest CBR as needed
                            double rCbr = r.getCbr();
                            if ((rCbr < lowestCBR) && (rCbr > 0.0)) {
                                lowestCBR = rCbr;
                            }
                            i++;
                        }
                        totalDepth = depth;
                        // Update Point.lowest
                        if (passedPoint.getCbr() != lowestCBR) {
                            passedPoint.setCbr(lowestCBR);
                        }
                    }
                });
                realmRecyclerView.smoothScrollToPosition(readingsRealmAdapter.getItemCount());

                if (newReading.getTotalDepth() > 1020) {
                    btnAppend.setTextColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Total depth exceeds standard rod length...", Toast.LENGTH_LONG).show();
                } else {
                    btnAppend.setTextColor(Color.WHITE);
                }
                //finish up by clearing selected points.
                clickedPos = -1;
                longClickedPos = -1;
                btnAppend.setBackgroundColor(Color.TRANSPARENT);
                btnAppend.setText("Append");
                readingsRealmAdapter.notifyDataSetChanged();

                // Set total depth helper value on screen.
                tvDepthTotal.setText("Depth: " + (totalDepth + (npDepth.getValue() * 5)));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public class ReadingsRealmAdapter extends RealmBasedRecyclerViewAdapter<Reading, ReadingsRealmAdapter.ViewHolder> {

        public class ViewHolder extends RealmViewHolder {

            public TextView readingsTextView;
            public TextView hammerTextView;
            public TextView blowsTextView;
            public TextView depthTextView;
            public TextView cbrTextView;
            public TextView totalTextView;
            private RelativeLayout rowLayout;

            public ViewHolder(RelativeLayout container) {
                super(container);
                this.readingsTextView = (TextView) container.findViewById(R.id.tvReadingNum);
                this.hammerTextView = (TextView) container.findViewById(R.id.tvHammerType);
                this.blowsTextView = (TextView) container.findViewById(R.id.tvBlows);
                this.depthTextView = (TextView) container.findViewById(R.id.tvDepth);
                this.cbrTextView = (TextView) container.findViewById(R.id.tvCbr);
                this.totalTextView = (TextView) container.findViewById(R.id.tvTotal);
                this.rowLayout = (RelativeLayout) container.findViewById(R.id.rlReading);
            }
        }

        public ReadingsRealmAdapter(
                Context context,
                RealmResults<Reading> realmResults,
                boolean automaticUpdate,
                boolean animateResults) {
            super(context,realmResults,automaticUpdate,animateResults);
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.listview_reading, viewGroup, false);
            ViewHolder vh = new ViewHolder((RelativeLayout)v);
            // TODO: setClickable was an attempt to highlight the row, not working...
            v.setClickable(true);
            return vh;
        }
        @Override
        public void onBindRealmViewHolder(final ViewHolder viewHolder, final int position) {
            final Reading reading = realmResults.get(position);
            if (position == clickedPos) {
                viewHolder.rowLayout.setBackgroundColor(Color.YELLOW);
            } else if (position == longClickedPos) {
                viewHolder.rowLayout.setBackgroundColor(Color.RED);
            } else {
                viewHolder.rowLayout.setBackgroundColor(Color.TRANSPARENT);
            }
            if (position == 0) {
                // Display zeroing reading
                viewHolder.blowsTextView.setText("");
                viewHolder.cbrTextView.setText("n/a");
            } else {
                // Display normal data
                viewHolder.blowsTextView.setText(String.valueOf(reading.getBlows()));
                viewHolder.cbrTextView.setText(String.format(Locale.US,"%.1f",reading.getCbr()));
            }
            viewHolder.depthTextView.setText(String.valueOf(reading.getDepth()));
            viewHolder.hammerTextView.setText(String.valueOf(reading.getHammer()));
            viewHolder.readingsTextView.setText(String.valueOf(reading.getReadingNum()));
            viewHolder.totalTextView.setText(String.valueOf(reading.getTotalDepth()));
            viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getBaseContext(), "You clicked a row: " + position, Toast.LENGTH_SHORT).show();
//                    Log.d("You Clicked row: ", String.valueOf(position));
                    if ((clickedPos == position) || (longClickedPos == position)) {
                        clickedPos = -1;
                        longClickedPos = -1;
                        view.setBackgroundColor(Color.TRANSPARENT);
                        btnAppend.setBackgroundColor(Color.TRANSPARENT);
                        btnAppend.setText("Append");

                    } else {
                        if (position != 0) {
                            // Only add above if not the first row which is the zeroing mark.
                            clickedPos = position;
                            longClickedPos = -1;
                            view.setBackgroundColor(Color.YELLOW);
                            btnAppend.setBackgroundColor(Color.YELLOW);
                            btnAppend.setText("Add\nAbove");
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.rowLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    Toast.makeText(getBaseContext(), "You long clicked row: " + String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    Log.d("You Long Clicked row: ", String.valueOf(position));
                    if (longClickedPos == position) {
                        longClickedPos = -1;
                        btnAppend.setBackgroundColor(Color.TRANSPARENT);
                        view.setBackgroundColor(Color.TRANSPARENT);
                        btnAppend.setText("Append");
                    } else {
                        clickedPos = -1;
                        longClickedPos = position;
                        view.setBackgroundColor(Color.RED);
                        btnAppend.setBackgroundColor(Color.RED);
                        btnAppend.setText("Replace\nSelected");
                    }
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
        @Override
        public void onItemSwipedDismiss(int position) {
            // Don't allow delete of first item
            if (position == 0) { 
                notifyDataSetChanged();
                Toast.makeText(Readings.this, "Unable to delete zeroing entry.\nPlease modify with long press.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Overridden to renumber the list items and refresh the list.
            realm.beginTransaction();
            //passedPoint.getReadings().remove(position);  // Thinking this will remove the point from the list....(might be causing bigger problems)
            realmResults.deleteFromRealm(position);
            int i = 0;
            int depth = 0;
            double lowestCBR = 100; // The lowest CBR recorded that is not 0

            for (Reading r : realmResults) {
                // Update Reading number
                r.setReadingNum(i);
                // Update Depth
                depth = depth + r.getDepth();
                r.setTotalDepth(depth);
                // Update lowest CBR as needed
                double rCbr = r.getCbr();
                if ((rCbr < lowestCBR) && (rCbr > 0.0)) {
                    lowestCBR = rCbr;
                }
                // TODO: Do I need to add all these readings back into the Point?
                i++;
            }
            totalDepth = depth;
            // Update Point.lowest if we have readings
            if ((realmResults.size() > 0) && (passedPoint.getCbr() != lowestCBR)) {
                passedPoint.setCbr(lowestCBR);
            }
            realm.commitTransaction();
            // Remove selections
            clickedPos = -1;
            longClickedPos = -1;
            btnAppend.setBackgroundColor(Color.TRANSPARENT);
            btnAppend.setText("Append");
            notifyDataSetChanged();

            // Set total depth helper value on screen.
            tvDepthTotal.setText("Depth: " + (totalDepth + (npDepth.getValue() * 5)));
        }
    }

    @Override
    public void onBackPressed() {
        // TODO: Update the lowestCBR for Point here or in onItemSwipedDismiss
        long projectId = passedPoint.getProject().getId();
        Intent intent = new Intent();
        intent.putExtra("passedProjectID", projectId);
        Log.d("ProjectID: ","From onBackPressed: " + String.valueOf(projectId));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm != null) {
            realm.close();
            realm = null;
        }
    }
}

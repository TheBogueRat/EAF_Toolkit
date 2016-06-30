package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;
// Added for import from gallery function
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.util.Log;

// Changed onClickListener to OnClickListener to fix issue, may not be correct way...Seems to be working
public class Draw extends AppCompatActivity implements OnClickListener{

    private DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton drawBtn, newBtn, saveBtn, colorBtn, opacityBtn;
    private String opacity;
    private int opacityInt;
    private float brushSize;
    private String brushColor;

    private SharedPreferences prefs;

    // For import from gallery
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView = (DrawingView)findViewById(R.id.drawing);

        prefs = this.getPreferences(Context.MODE_PRIVATE);
        opacity = prefs.getString("keyOpacity", "#FF");
        opacityInt = prefs.getInt("keyOpacityInt", 250);
        brushColor = prefs.getString("keyBrushColor", "000000");
        brushSize = prefs.getFloat("keyBrushSize", 20);
        drawView.setColor(opacity + brushColor);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        opacityBtn = (ImageButton)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        colorBtn = (ImageButton)findViewById(R.id.color_btn);
        colorBtn.setOnClickListener(this);

        //galleryBtn = (ImageButton)findViewById(R.id.gallery_btn);
        //galleryBtn.setOnClickListener(this);

        // Restore a drawing in progress
        FileInputStream fis;
        Drawable drawable = null;
        try {
            fis = openFileInput("drawingInProgress");
            drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(fis));
            fis.close();
        } catch (FileNotFoundException e) {
            // Don't need to do anything, user probably never edited the image.
            e.printStackTrace();
        } catch (IOException e) {
            // Unable to connect to internal storage?
            e.printStackTrace();
        }
        // set as background image.
        drawView.setBackground(drawable);
    }

    private String getOpacityString(){
        return "#" + Integer.toString(opacityInt, 16);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").

        prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("keyOpacity", opacity);
        prefsEditor.putInt("keyOpacityInt", opacityInt);
        prefsEditor.putString("keyBrushColor", brushColor);
        prefsEditor.putFloat("keyBrushSize", brushSize);
        prefsEditor.apply();

        FileOutputStream fos;
        drawView.setDrawingCacheEnabled(true);
        Bitmap drawViewImage = drawView.getDrawingCache();
        try {
            fos = openFileOutput("drawingInProgress", Context.MODE_PRIVATE);
            drawViewImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        drawView.destroyDrawingCache();
    }

    @Override
    public void onClick(View view){

        //respond to clicks
        if(view.getId()==R.id.draw_btn){
            //Brush Size button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushSize = smallBrush;
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //Eraser button to clear Background or Annotations
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Clear?");
            newDialog.setItems(R.array.clearOptions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position of the selected item
                    switch(which) {
                        case 0:
                            // Clear background an annotations
                            drawView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            drawView.startNew();
                            break;
                        case 1:
                            // Clear Background
                            drawView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 2:
                            // Clear Annotations
                            drawView.startNew();
                            break;
                        default:
                            // Do nothing
                    }
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();

        } else if(view.getId()==R.id.color_btn){
            //Brush Color button clicked
            final Dialog colorDialog = new Dialog(this);
            colorDialog.setTitle("Select Color:");
            colorDialog.setContentView(R.layout.brush_color);

            ImageButton camoBlackBtn = (ImageButton)colorDialog.findViewById(R.id.camoBlack);
            camoBlackBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "000000";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoGreyBtn = (ImageButton)colorDialog.findViewById(R.id.camoGray);
            camoGreyBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "666666";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoRedBtn = (ImageButton)colorDialog.findViewById(R.id.camoRed);
            camoRedBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "FF0000";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoBlueBtn = (ImageButton)colorDialog.findViewById(R.id.camoBlue);
            camoBlueBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "0000FF";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoGreenBtn = (ImageButton)colorDialog.findViewById(R.id.camoGreen);
            camoGreenBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "66CC00";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoBrownBtn = (ImageButton)colorDialog.findViewById(R.id.camoBrown);
            camoBrownBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    brushColor = "654321";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            ImageButton camoWhiteBtn = (ImageButton)colorDialog.findViewById(R.id.camoWhite);
            camoWhiteBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO: User v.getTag for all...
                    brushColor = v.getTag().toString();
                    //brushColor = "FFFFFF";
                    drawView.setColor(opacity + brushColor);
                    colorDialog.dismiss();
                }
            });
            colorDialog.show();
        } else if (view.getId()==R.id.opacity_btn) {
            //Brush Size button clicked
            final Dialog opacityDialog = new Dialog(this);
            opacityDialog.setTitle("Adjust Brush Opacity:");
            opacityDialog.setContentView(R.layout.brush_opacity);
            final ImageView ivOpacity = (ImageView) opacityDialog.findViewById(R.id.imageViewOpacity);
            ivOpacity.setImageDrawable(getResources().getDrawable(R.drawable.br));
            ivOpacity.setImageAlpha(opacityInt);
            final TextView tvOpacity = (TextView) opacityDialog.findViewById(R.id.textOpacity);
            tvOpacity.setText(Integer.toString(opacityInt/25*10)+"%");
            SeekBar sbOpacity = (SeekBar) opacityDialog.findViewById(R.id.seekBarOpacity);
            sbOpacity.setProgress(opacityInt/25-1);
            sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    opacityInt = (seekBar.getProgress() + 1) * 25;
                    //opacity = "#" + Integer.toHexString(opacityInt);
                    tvOpacity.setText(Integer.toString(opacityInt/25*10)+"%");
                    ivOpacity.setImageAlpha(opacityInt);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    opacity = getOpacityString();
                    drawView.setColor(opacity + brushColor);
                }
            });
            opacityDialog.show();
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.d("EAF Toolkit", "Getting image from data");
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                if(cursor!=null && cursor.getCount()>0 ) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                }

                // Convert bitmap to drawable and set as background image.
                Drawable drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imgDecodableString));
                drawView.setBackground(drawable);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

}

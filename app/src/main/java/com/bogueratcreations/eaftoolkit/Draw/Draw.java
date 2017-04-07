package com.bogueratcreations.eaftoolkit.Draw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bogueratcreations.eaftoolkit.R;
import com.bogueratcreations.eaftoolkit.common.CapturePhotoUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.Manifest;
import android.content.pm.PackageManager;

// Changed onClickListener to OnClickListener to fix issue, may not be correct way...Seems to be working
public class Draw extends AppCompatActivity implements OnClickListener {

    private DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private String opacity;
    private int opacityInt;
    private float brushSize;
    private String brushColor;

    private SharedPreferences prefs;

    // For import from gallery
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;

    static final int MY_PERMISSIONS_REQUEST_SAVE_TO_EXTERNAL_STORAGE = 7011;
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7012;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView = (DrawingView) findViewById(R.id.drawing);

        prefs = this.getPreferences(Context.MODE_PRIVATE);
        opacity = prefs.getString("keyOpacity", "#FF");
        opacityInt = prefs.getInt("keyOpacityInt", 250);
        brushColor = prefs.getString("keyBrushColor", "000000");
        brushSize = prefs.getFloat("keyBrushSize", 20);
        drawView.setColor(opacity + brushColor);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        ImageButton drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        ImageButton newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        ImageButton saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        ImageButton opacityBtn = (ImageButton) findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        ImageButton colorBtn = (ImageButton) findViewById(R.id.color_btn);
        colorBtn.setOnClickListener(this);

        // Restore a drawing in progress
        FileInputStream fis;
        Drawable drawable;
        try {
            fis = openFileInput("drawingInProgress");
            drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(fis));
            // set as background image.
            drawView.setBackground(drawable);
            fis.close();
        } catch (IOException e) {
            // Unable to connect to internal storage?
            e.printStackTrace();
        }
    }

    private String getOpacityString() {
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

        //FileOutputStream fos;
        drawView.setDrawingCacheEnabled(true);
        Bitmap drawViewImage = drawView.getDrawingCache();
        // create a copy to pass for storage...
        Bitmap viewImageCopy = drawViewImage.copy(drawViewImage.getConfig(), true);
        drawView.destroyDrawingCache();
        BitmapWorkerTask task = new BitmapWorkerTask();
        task.execute(viewImageCopy);
    }

    class BitmapWorkerTask extends AsyncTask<Bitmap, Void, Void> {

        // Store image in background.
        @Override
        protected Void doInBackground(Bitmap... params) {
            Bitmap image = params[0];
            FileOutputStream fos;
            try {
                fos = openFileOutput("drawingInProgress", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @Override
    public void onClick(View view) {

        //respond to clicks
        if (view.getId() == R.id.draw_btn) {
            setBrushDimens();
        } else if (view.getId() == R.id.new_btn) {
            erase();
        } else if (view.getId() == R.id.save_btn) {
            saveImage();
        } else if (view.getId() == R.id.color_btn) {
            setBrushColor();
        } else if (view.getId() == R.id.opacity_btn) {
            setBrushOpacity();
        }
    }

    // Methods for UI buttons/menu items
    private void setBrushDimens() {
        //Brush Size button clicked
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.brush_chooser);
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                brushSize = smallBrush;
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    private void setBrushColor() {
        //Brush Color button clicked
        final Dialog colorDialog = new Dialog(this);
        colorDialog.setTitle("Select Color:");
        colorDialog.setContentView(R.layout.brush_color);

        ImageButton camoBlackBtn = (ImageButton) colorDialog.findViewById(R.id.camoBlack);
        camoBlackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "000000";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoGreyBtn = (ImageButton) colorDialog.findViewById(R.id.camoGray);
        camoGreyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "666666";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoRedBtn = (ImageButton) colorDialog.findViewById(R.id.camoRed);
        camoRedBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "FF0000";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoBlueBtn = (ImageButton) colorDialog.findViewById(R.id.camoBlue);
        camoBlueBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "0000FF";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoGreenBtn = (ImageButton) colorDialog.findViewById(R.id.camoGreen);
        camoGreenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "66CC00";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoBrownBtn = (ImageButton) colorDialog.findViewById(R.id.camoBrown);
        camoBrownBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                brushColor = "654321";
                drawView.setColor(opacity + brushColor);
                colorDialog.dismiss();
            }
        });
        ImageButton camoWhiteBtn = (ImageButton) colorDialog.findViewById(R.id.camoWhite);
        camoWhiteBtn.setOnClickListener(new OnClickListener() {
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
    }

    private void setBrushOpacity() {
        //Brush Opacity button clicked
        final Dialog opacityDialog = new Dialog(this);
        opacityDialog.setTitle("Adjust Brush Opacity:");
        opacityDialog.setContentView(R.layout.brush_opacity);
        final ImageView ivOpacity = (ImageView) opacityDialog.findViewById(R.id.imageViewOpacity);
        ivOpacity.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.br, null));
        ivOpacity.setImageAlpha(opacityInt);
        final TextView tvOpacity = (TextView) opacityDialog.findViewById(R.id.textOpacity);
        String opacityPercent = Integer.toString(opacityInt / 25 * 10) + "%";
        tvOpacity.setText(opacityPercent);
        SeekBar sbOpacity = (SeekBar) opacityDialog.findViewById(R.id.seekBarOpacity);
        sbOpacity.setProgress(opacityInt / 25 - 1);
        sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                opacityInt = (seekBar.getProgress() + 1) * 25;
                String opacityPercent = Integer.toString(opacityInt / 25 * 10) + "%";
                tvOpacity.setText(opacityPercent);
                ivOpacity.setImageAlpha(opacityInt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                opacity = getOpacityString();
                drawView.setColor(opacity + brushColor);
            }
        });
        opacityDialog.show();
    }

    private void erase() {
        //Eraser button to clear Background or Annotations
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("Clear?");
        newDialog.setItems(R.array.clearOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position of the selected item
                switch (which) {
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

    private void saveImage() {
        // Need to check version because of changes in permissions handling.
        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(Draw.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Draw.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Ask again without any special message.
                    ActivityCompat.requestPermissions(Draw.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_SAVE_TO_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Draw.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_SAVE_TO_EXTERNAL_STORAGE);
                }
            }else{
                saveDialog();
            }
        }else {
            saveDialog();
        }
    }

    private void saveDialog() {

        // Code to save the drawing...
        AlertDialog.Builder saveImgDialog = new AlertDialog.Builder(this);
        saveImgDialog.setTitle("Save drawing");
        saveImgDialog.setMessage("Save drawing to device Gallery?");
        saveImgDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                drawView.setDrawingCacheEnabled(true);
                String imgSaved = CapturePhotoUtils.insertImage(
                        getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "EAF Toolkit Drawing");
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
        saveImgDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveImgDialog.show();
    }

    public void loadImageFromGallery(View view) {

        // Need to check version because of changes in permissions handling.
        if (Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(Draw.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Draw.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Try to request permissions again without a message.
                    ActivityCompat.requestPermissions(Draw.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Draw.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }else{
                // Have permission so load image image.
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        }else {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SAVE_TO_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // gallery-related task you need to do.
                    saveDialog();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved without the proper permissions.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be loaded without the appropriate permissions.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
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
                int myWidth = drawView.getWidth();
                int myHeight = drawView.getHeight();
                // Convert bitmap to drawable, resize and set as background image.
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imgDecodableString));
                // Extract bitmap and crop center
                Bitmap bitmap = scaleRotateCenterCrop(bitmapDrawable.getBitmap(), myHeight, myWidth);
                bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
                // Clear screen and set background image.
                drawView.startNew();
                drawView.setBackground(bitmapDrawable);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap scaleRotateCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Rotates image if landscape to retain as much of the image as possible after cropping
        if (sourceWidth > sourceHeight) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            // Update width/height values
            sourceWidth = sourceHeight + sourceWidth;
            sourceHeight = sourceWidth - sourceHeight;
            sourceWidth = sourceWidth - sourceHeight;
        }
        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
}

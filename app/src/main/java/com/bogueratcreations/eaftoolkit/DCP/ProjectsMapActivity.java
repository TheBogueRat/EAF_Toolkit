package com.bogueratcreations.eaftoolkit.DCP;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bogueratcreations.eaftoolkit.R;
import com.bogueratcreations.eaftoolkit.common.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

public class ProjectsMapActivity extends AppCompatActivity implements
        LocationListener,
        OnMapReadyCallback,
        OnMarkerDragListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    String DEBUG_STRING = "EAFToolkit";

    GoogleMap mMap;
    LatLng storedLoc = new LatLng(30.349108, -87.316360);  // EAF Schoolhouse?
    LatLng currentLoc = new LatLng(34.893192, -76.891700);
    LatLng oldLoc;
    private long projectId;
    private double passedLat;
    private double passedLong;
    private boolean receivedLoc = false;  // True for debugging, forcing the schoolhouse as the passed location
    private boolean hasPermission = false;
    private final int MY_LOCATION_REQUEST_CODE = 58;
    private MapView mapView;
    private Button btnStore;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab = (FloatingActionButton) findViewById(R.id)
        //setTitle("Project Location");

        projectId = getIntent().getLongExtra("projectId", -1);
        // Get lat/long and if not -1, put into location(LatLng) object
        passedLat = getIntent().getDoubleExtra("latitude", -1);
        passedLong = getIntent().getDoubleExtra("longitude", -1);
        if ((passedLong != -1) && (passedLat != -1)) {
            Log.d(DEBUG_STRING, "Received a valid lat/long");
            storedLoc = new LatLng(passedLat, passedLong);
            receivedLoc = true;
            // Use this loc and actual loc to zoom map otherwise center on current loc.
        } else {
            Log.d(DEBUG_STRING, "Failed to retireve a valid Lat/Long");
            Log.d(DEBUG_STRING, "Lat: " + passedLat + "  Lng: " + passedLong);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        btnStore = (Button) findViewById(R.id.btnStore);
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedLoc) {
                    // Already saved a location, ask user to override.
                    Log.d("EAFToolkit", "Already have a location, asking for overwrite....");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProjectsMapActivity.this);
                    builder.setMessage("Change location?");
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Save Location
                            savePos();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Just return to Point view.
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    savePos();
                }
            }
        });
        mapView = (MapView) findViewById(R.id.mvProject);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
    }

    public void savePos() {
        Log.d("EAFToolkit", "Starting savePos()");
        // record current location, already verified that we have permissions for myLocation.
        Log.d("EAFToolkit", "savePos mMap is " + mMap);
        storedLoc = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        Log.d("EAFToolkit", "Location from mGoogleMap is: " + storedLoc);
//        SharedPreferences sharedPreferences = context.getSharedPreferences("com.bogueratcreations.cherrypointairshow.settings", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong("Latitude", Double.doubleToLongBits(storedLoc.latitude));
//        editor.putLong("Longitude", Double.doubleToLongBits(storedLoc.longitude));
//        editor.apply();

        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(storedLoc)
                .title("Project Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cone_map_marker_sm))
                .draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(storedLoc, 14));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        //tryToSetMyLocationEnabled();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //mMap.getUiSettings().setZoomGesturesEnabled(true);
        if (receivedLoc) {
            mMap.addMarker(new MarkerOptions()
                    .position(storedLoc)
                    .title("Project Location")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cone_map_marker_sm)));
        }
        // retrieve current location
        if (hasPermission) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    onLocationChanged(location);
                }
                //currentLoc = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                Log.d("EAFToolkit","Just grabbed current location in onMapReady");
            } catch (Exception e) {
                Log.d("EAFToolkit", "Caught Exception: " + e.getMessage());
            }
        }
        // Set Listeners for marker events
        mMap.setOnMarkerDragListener(this);

        Log.d("EAFToolkit", "mapReady mMap is " + mMap);
    }
//
//    public void tryToSetMyLocationEnabled() {
//        // Need to check version because of changes in permissions handling.
//        if (Build.VERSION.SDK_INT >= 23) {
//            // Here, thisActivity is the current activity
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    // Ask again without any special message.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_LOCATION_REQUEST_CODE);
//                } else {
//                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            MY_LOCATION_REQUEST_CODE);
//                }
//            } else {
//                enableService();
//            }
//        } else {
//            enableService();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_LOCATION_REQUEST_CODE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the thing.
//                    enableService();
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast unsavedToast = Toast.makeText(this,
//                            "Oops! Your current location cannot be displayed without permission.  Please grant permission for location services.", Toast.LENGTH_LONG);
//                    unsavedToast.show();
//                    // Return to previous screen.
//                    onBackPressed();
//                }
//                break;
//            }
//        }
//    }
//
//    public void enableService() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Already checked or I wouldn't be here.  But just in case...
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        hasPermission = true;
//        btnStore.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onBackPressed() {
        // send passedProjectID, Lat, and Long back to original activity
        //long projectId = passedPoint.getProject().getId();
        Intent intent = new Intent();
        intent.putExtra("passedProjectID", projectId);
        intent.putExtra("lattitude", storedLoc.latitude);
        intent.putExtra("longitude", storedLoc.longitude);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            hasPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO: Detect stored location and show both points for simple navigation.
        currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        if (receivedLoc) {
            // zoom so stored location can be displayed in relation to current location.
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLoc);
            builder.include(storedLoc);
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // TODO: Request confirmation to save?
        oldLoc = storedLoc;
        storedLoc = marker.getPosition();
    }
}

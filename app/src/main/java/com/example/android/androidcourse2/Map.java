package com.example.android.androidcourse2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.TrackingSettings;


public class Map extends AppCompatActivity {
    private MapView mMapView = null;
    private MapboxMap mMapboxMap;
    private double Lat = 12.9810816;
    private double Lon = 77.6368034;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setTag(true);
        //mMapView.setAccessToken(ApiAccess.getToken(this));
        mMapView.onCreate(savedInstanceState);

        Lat = this.getIntent().getDoubleExtra("lat", 0);
        Lon = this.getIntent().getDoubleExtra("lon", 0);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;

                int paddingLeft = 2;
                int paddingBottom = 2;
                int paddingRight = 2;
                mapboxMap.setPadding(paddingLeft, 2, paddingRight, paddingBottom);
                //mapboxMap.setStyleUrl(Style.LIGHT);//OPTIONAL
                mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

                moveToLatLon(Lat, Lon);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(2, menu);
        return true;
    }

    private void toggleGps(boolean enable) {
        try {
            // Enable user location
            mMapboxMap.setMyLocationEnabled(enable);

            TrackingSettings trackingSettings = mMapboxMap.getTrackingSettings();
            trackingSettings.setDismissTrackingOnGesture(false);
            trackingSettings.setMyLocationTrackingMode(enable ? MyLocationTracking.TRACKING_FOLLOW : MyLocationTracking.TRACKING_NONE);
        } catch (SecurityException e) {
            // permission not granted is handled in FeatureOverviewActivity
            finish();
        }
    }

    private void moveToLatLon(double lat, double lon) {
        toggleGps(false);

        LatLng current = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .zoom(15)
                .target(current)
                .build();//.zoom(16).bearing(40).tilt(45)

        mMapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMapboxMap.addMarker(new MarkerOptions().title("Center map").position(current));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            /*case R.id.action_user_tracking:
                if (mMapboxMap != null) {
                    toggleGps(true);
                }
                return true;

            case R.id.action_bangalore:
                if (mMapboxMap != null) {
                    moveToBangalore();
                }
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

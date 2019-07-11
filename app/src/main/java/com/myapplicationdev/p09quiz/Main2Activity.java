package com.myapplicationdev.p09quiz;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main2Activity extends AppCompatActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                Intent i = getIntent();
                String lat = i.getStringExtra("lat");
                String lng = i.getStringExtra("lng");
                LatLng sample = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(sample,
                        14));

                final Marker cp = map.addMarker(new
                        MarkerOptions()
                        .position(sample)
                        .title("Your Location is here")
                        .snippet(lat+","+lng));


                UiSettings ui = map.getUiSettings();

                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);
                ui.setMapToolbarEnabled(true);

                int permissionCheck = ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                    ActivityCompat.requestPermissions(Main2Activity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                }

            }
        });

    }
}
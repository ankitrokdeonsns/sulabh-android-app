package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import com.example.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LaunchActivity extends Activity {

    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location location= locationManager.getLastKnownLocation(provider);
        if(map!=null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng myPosition = new LatLng(latitude, longitude);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(LaunchActivity.this, DetailsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            map.addMarker(new MarkerOptions().position(myPosition));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
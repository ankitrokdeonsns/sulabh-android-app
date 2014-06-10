package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.thoughtworks.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thoughtworks.sulabh.handler.InternetConnectionHandler;
import com.thoughtworks.sulabh.handler.MapDisplayHandler;
import com.thoughtworks.sulabh.helper.ImageMapper;
import com.thoughtworks.sulabh.model.Loo;

import java.util.List;

import static com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

public class LaunchActivity extends Activity implements OnMapLongClickListener {

    private LocationManager locationManager;
    private GoogleMap map;
    private Loo selectedLoo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null && extras.getBoolean("isPressed") == true){
            String toastMessage = String.valueOf(extras.get("toastMessage"));
            Toast.makeText(getApplicationContext(), toastMessage , Toast.LENGTH_LONG).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        new InternetConnectionHandler(this.getApplicationContext()) {

            @Override
            public void executeIfConnectedToInternet() {
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                new MapDisplayHandler(map, LaunchActivity.this).displayMap();
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(LaunchActivity.this, DetailsActivity.class);
                        intent.putExtra("Loo", selectedLoo);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void executeIfNotConnectedToInternet() {
                showExitAlert();
            }

        }.checkForConnectivity();
    }

    private void showExitAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Network unavailable...");
        alertDialog.setMessage("Network seems to be unavailable!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                LaunchActivity.this.finish();
            }
        });
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            int EXIT = 0;
            Intent yesAction = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            alertMessageBuilder("Your GPS seems to be disabled, do you want to enable it?", yesAction, EXIT);
        }
    }

    private void alertMessageBuilder(String message, final Intent yesAction, final int status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(yesAction);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        if (status == 0)
                            System.exit(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertMessageBuilderForAddLoo(final Intent yesAction) {
        String currentPositionMessage = "On current location";
        String differentPositionMessage = "Long tap on map for other location";
        String items[] = {currentPositionMessage, String.valueOf(differentPositionMessage)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a loo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                if (choice == 0)
                    startActivity(yesAction);
                else
                    Toast.makeText(getApplicationContext(),"Note : Long tap on map to add loo",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent(LaunchActivity.this, AddLooActivity.class);
        intent.putExtra("coordinates", new double[]{latLng.latitude, latLng.longitude});
        alertMessageBuilder("Do you want to add a toilet?", intent, 1);
    }

    public void populateMarkers(final GoogleMap map, final List<Loo> loos) {
        for (final Loo loo : loos) {
            LatLng markerPosition = new LatLng(loo.getCoordinates()[0], loo.getCoordinates()[1]);
            map.addMarker(new MarkerOptions().position(markerPosition));
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                for (Loo loo : loos) {
                    if (loo.isSamePositionAs(marker.getPosition())) {
                        selectedLoo = loo;
                    }
                }
                new ImageMapper(LaunchActivity.this).mapImages();
                marker.showInfoWindow();
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600000, 10, locationListener);
        return true;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public GoogleMap getMap() {
        return map;
    }

    public Loo getSelectedLoo() {
        return selectedLoo;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            LatLng myPosition = new LatLng(latitude, longitude);
            Intent intent = new Intent(LaunchActivity.this, AddLooActivity.class);
            intent.putExtra("coordinates", new double[]{myPosition.latitude, myPosition.longitude});
            alertMessageBuilderForAddLoo(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
}
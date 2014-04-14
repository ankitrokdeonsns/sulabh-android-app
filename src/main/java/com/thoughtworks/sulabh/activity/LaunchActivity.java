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
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.LooList;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class LaunchActivity extends Activity {

    private LocationManager locationManager;
    String url = "http://10.12.20.133:3000/locations";
    private LooList loos;
    private LatLng markerPosition;
    private List<Loo> looList;
    private Loo tempLoo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (map != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng myPosition = new LatLng(latitude, longitude);
            new ResHandler(callback()).execute(url);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Intent intent = new Intent(LaunchActivity.this, DetailsActivity.class);
                    intent.putExtra("Operational","yes");
                    intent.putExtra("Hygienic", "yes");
                    intent.putExtra("Free/Paid", "yes");
                    intent.putExtra("Kind", "indian");
                    intent.putExtra("Suitable For", "women");
                    startActivity(intent);
                    return true;
                }

            });
            ObjectMapper mapper = new ObjectMapper();
            try {
                loos = mapper.readValue("{\"locations\":[{\"name\":\"Pheonix Market City\",\"coordinates\":[40,35]," +
                        "\"operational\":\"yes\",\"hygienic\":\"yes\",\"paid\":\"no\",\"kind\":\"western\",\"compatibility\":\"men\"}," +
                        "{\"name\":\"KP Mall\",\"coordinates\":[45,32]," +
                        "\"operational\":\"yes\",\"hygienic\":\"yes\",\"paid\":\"no\",\"kind\":\"western\",\"compatibility\":\"men\"}]}",LooList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            looList = loos.getLocations();
            System.out.println("**********" + looList.get(0).getName() + "****************" + looList.get(1).getCoordinates());
            map.addMarker(new MarkerOptions().position(myPosition));

            for (Loo loo : looList) {
                markerPosition = new LatLng(loo.getCoordinates().get(0),loo.getCoordinates().get(1));
                map.addMarker(new MarkerOptions().position(markerPosition));
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private Callback<JSONObject> callback(){
        return new Callback<JSONObject>() {

            @Override
            public void execute(JSONObject object) throws IOException {
//                ObjectMapper mapper = new ObjectMapper();
//                looList = mapper.readValue("{\"locations\":[{\"name\":\"Pheonix Market City\",\"coordinates\":[40,35]},{\"name\":\"KP Mall\",\"coordinates\":[45,32]}]}",LooList.class);
//                List<Loo> looList = looList.getLocations();
//                System.out.println("@@@@@@@@@@@@@@@@: "+looList.get(0).getName());
            }
        };
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
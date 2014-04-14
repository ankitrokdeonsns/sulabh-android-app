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
    private List<Loo> looList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location currentLocation = locationManager.getLastKnownLocation(provider);
        map.setMyLocationEnabled(true);
        if (map != null) {
            new ResHandler(callback()).execute(url);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Loo selectedLoo = getSelectedLoo(marker);
                    Intent intent = new Intent(LaunchActivity.this, DetailsActivity.class);
                    intent.putExtra("Operational", selectedLoo.getOperational());
                    intent.putExtra("Hygienic", selectedLoo.getHygienic());
                    intent.putExtra("Free/Paid", selectedLoo.getPaid());
                    intent.putExtra("Kind", selectedLoo.getKind());
                    intent.putExtra("Suitable For", selectedLoo.getCompatibility());
                    startActivity(intent);
                    return true;
                }

            });
            ObjectMapper mapper = new ObjectMapper();
            try {
                loos = mapper.readValue("{\"locations\":[{\"name\":\"Pheonix Market City\",\"coordinates\":[40,33]," +
                        "\"operational\":\"yes\",\"hygienic\":\"yes\",\"paid\":\"yes\",\"kind\":\"western\",\"compatibility\":\"men\"}," +
                        "{\"name\":\"KP Mall\",\"coordinates\":[45,32]," +
                        "\"operational\":\"yes\",\"hygienic\":\"yes\",\"paid\":\"no\",\"kind\":\"indian\",\"compatibility\":\"men\"}]}",LooList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            looList = loos.getLocations();

            for (Loo loo : looList) {
                LatLng markerPosition = new LatLng(loo.getCoordinates().get(0),loo.getCoordinates().get(1));
                //remove field
                map.addMarker(new MarkerOptions().position(markerPosition));
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private Loo getSelectedLoo(Marker marker) {
        double markerLongitude = marker.getPosition().longitude;
        double markerLatitude = marker.getPosition().latitude;
        for (Loo loo : looList) {
            if(areDoublesEqual(loo.getCoordinates().get(0), markerLatitude) && areDoublesEqual(loo.getCoordinates().get(1), markerLongitude))
                return loo;
        }
        return null;
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
    public static boolean areDoublesEqual(double a, double b){
        double DELTA = 0.0000005;
        return Math.abs(a - b) < DELTA;
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
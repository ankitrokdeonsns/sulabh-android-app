package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thoughtworks.sulabh.Loo;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class LaunchActivity extends Activity {

	private LocationManager locationManager;
	private LatLng markerPosition;
	private GoogleMap map;
	Loo selectedLoo;
	private ProgressDialog progressDialog;
	private Location currentLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null){
			String toastMessage = String.valueOf(extras.get("toastMessage"));
			System.out.println("toastMessage = " + toastMessage);
			Toast.makeText(getApplicationContext(), toastMessage , Toast.LENGTH_LONG).show();
		}

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		String provider = locationManager.getBestProvider(new Criteria(), true);
		currentLocation = locationManager.getLastKnownLocation(provider);
		map.setMyLocationEnabled(true);
		if (map != null) {
			LatLng myPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			progressDialog = new ProgressDialog(LaunchActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("Loading...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setProgress(0);
			progressDialog.show();
			new ResHandler(callback(), myPosition).execute();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		}
	}

	private void populateMarkers(GoogleMap map, final List<Loo> loos) {
		for (final Loo loo : loos) {
			markerPosition = new LatLng(loo.getCoordinates()[0],loo.getCoordinates()[1]);
			map.addMarker(new MarkerOptions().position(markerPosition));
		}
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				for (Loo loo : loos) {
					if(loo.isSamePositionAs(marker.getPosition())){
						selectedLoo = loo;
					}
				}
				Intent intent = new Intent(LaunchActivity.this, DetailsActivity.class);
				intent.putExtra("Loo", selectedLoo);
				startActivity(intent);
				return true;
			}
		});

	}

	private Callback<JSONObject> callback(){
		return new Callback<JSONObject>() {
			@Override
			public void execute(List<Loo> loos) throws IOException {
				populateMarkers(map, loos);
				progressDialog.dismiss();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(LaunchActivity.this, AddLooActivity.class);
		intent.putExtra("coordinates",new double[]{currentLocation.getLatitude(), currentLocation.getLongitude()});
		startActivity(intent);
		return true;
	}
}
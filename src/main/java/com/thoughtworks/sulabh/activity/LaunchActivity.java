package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.Toast;
import com.example.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.handler.MapDisplayHandler;

import java.util.List;

import static com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

public class LaunchActivity extends Activity implements OnMapLongClickListener{

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
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		new MapDisplayHandler(map, this).displayMap();
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
						if(status == 0)
							System.exit(0);
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		Intent intent = new Intent(LaunchActivity.this, AddLooActivity.class);
		intent.putExtra("coordinates", new double[]{latLng.latitude, latLng.longitude});
		alertMessageBuilder("Do you want to add a toilet?", intent, 1);
	}

	public void populateMarkers(GoogleMap map, final List<Loo> loos) {
		for (final Loo loo : loos) {
			LatLng markerPosition = new LatLng(loo.getCoordinates()[0], loo.getCoordinates()[1]);
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

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public GoogleMap getMap() {
		return map;
	}
}
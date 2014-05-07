package com.thoughtworks.sulabh.activity;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.thoughtworks.sulabh.Callback;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.handler.ResHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapDisplayHandler {
	private GoogleMap map;
	private final LaunchActivity launchActivity;
	private ProgressDialog progressDialog;

	public MapDisplayHandler(GoogleMap map, LaunchActivity launchActivity, ProgressDialog progressDialog){
		this.map = map;
		this.launchActivity = launchActivity;
		this.progressDialog = progressDialog;
	}

	void getLocation() {
		launchActivity.getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		launchActivity.getMap().setMyLocationEnabled(true);
		launchActivity.getMap().setOnMapLongClickListener(this.launchActivity);
	}

	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			LatLng myPosition = new LatLng(latitude, longitude);
			new ResHandler(callback(), myPosition).execute();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

		public void onProviderEnabled(String provider) {}

		public void onProviderDisabled(String provider) {}
	};

	private Callback<JSONObject> callback(){
		return new Callback<JSONObject>() {
			@Override
			public void execute(List<Loo> loos) throws IOException {
				launchActivity.populateMarkers(map, loos);
				progressDialog.dismiss();
			}
		};
	}
}
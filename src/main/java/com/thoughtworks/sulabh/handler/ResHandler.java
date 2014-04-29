package com.thoughtworks.sulabh.handler;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.Callback;
import com.thoughtworks.sulabh.gateways.SulabhGateway;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ResHandler extends AsyncTask<String, String, List<Loo>> {

	private Callback<JSONObject> callback;
	private LatLng myPosition;

	public ResHandler(Callback<JSONObject> callback, LatLng myPosition){
		this.callback = callback;
		this.myPosition = myPosition;
	}

	@Override
	protected List<Loo> doInBackground(String... uri) {
		int radius = 2;
		double latitude = myPosition.latitude;
		double longitude = myPosition.longitude;
		List<Loo> loos = new SulabhGateway().getLoos(latitude, longitude, radius);
		return loos;
	}

	@Override
	protected void onPostExecute(List<Loo> loos) {
		super.onPostExecute(loos);
		try {
			callback.execute(loos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
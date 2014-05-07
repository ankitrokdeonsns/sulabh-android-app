package com.thoughtworks.sulabh.handler;

import android.os.AsyncTask;
import com.thoughtworks.sulabh.AddCallback;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

import java.io.IOException;

public class AddResponseHandler extends AsyncTask<String, String, Boolean> {

	private AddCallback<Boolean> callback;
	private Loo looToAdd;

	public AddResponseHandler(AddCallback<Boolean> callback, Loo looToAdd){
		this.callback = callback;
		this.looToAdd = looToAdd;
	}

	@Override
	protected Boolean doInBackground(String... uri) {
		return new SulabhGateway().addLoo(looToAdd);
	}

	@Override
	protected void onPostExecute(Boolean isAdded) {
		super.onPostExecute(isAdded);
		try {
			callback.execute(isAdded);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
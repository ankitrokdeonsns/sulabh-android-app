package com.thoughtworks.sulabh.handler;

import android.os.AsyncTask;
import com.thoughtworks.sulabh.model.Loo;
import com.thoughtworks.sulabh.helper.UpdateCallback;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

import java.io.IOException;

public class UpdateResponseHandler extends AsyncTask<String, String, Boolean> {

	private UpdateCallback<Boolean> callback;
	private Loo looToAdd;

	public UpdateResponseHandler(UpdateCallback<Boolean> callback, Loo looToAdd){
		this.callback = callback;
		this.looToAdd = looToAdd;
	}

	@Override
	protected Boolean doInBackground(String... uri) {
		return new SulabhGateway().updateLoo(looToAdd);
	}

	@Override
	protected void onPostExecute(Boolean isUpdated) {
		super.onPostExecute(isUpdated);
		try {
			callback.execute(isUpdated);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
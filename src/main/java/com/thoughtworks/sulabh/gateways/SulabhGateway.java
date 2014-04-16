package com.thoughtworks.sulabh.gateways;

import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.LooList;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class SulabhGateway {

	private final HttpClient httpclient;
	private LooList loos;

	public SulabhGateway(){
		httpclient = new DefaultHttpClient();
	}

	public List<Loo> getLoos(double latitude, double longitude, int radius) {
		try {
			String url = "http://10.12.124.216:3000/locations?latitude=" + latitude + "&longitude=" + longitude;
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				String responseString = out.toString();
				ObjectMapper mapper = new ObjectMapper();
				loos = mapper.readValue(String.valueOf(responseString), LooList.class);
				out.close();
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loos.getLocations();
	}
}
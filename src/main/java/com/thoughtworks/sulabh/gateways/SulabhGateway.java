package com.thoughtworks.sulabh.gateways;

import android.os.StrictMode;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.LooList;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SulabhGateway {

	private final HttpClient httpclient;
	private LooList loos;

	public SulabhGateway(){
		httpclient = new DefaultHttpClient();
	}

	public List<Loo> getLoos(double latitude, double longitude, int radius) {
		try {
			String url = "http://10.12.124.93:3000/locations?latitude=" + latitude + "&longitude=" + longitude;
			System.out.println("url = " + url);
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

	public boolean addLoo(Loo loo) {
		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://10.12.124.32:3000/add";
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = getNameValuePairs(loo);

			System.out.println("**********PARAMS ADDED");
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
			int code = responsePOST.getStatusLine().getStatusCode();
			System.out.println("****************code = " + code);
			System.out.println("**********GOT RESPONSE");
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				String responseString = EntityUtils.toString(resEntity);
				System.out.println("RESPONSE = "+ responseString);
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private List<NameValuePair> getNameValuePairs(Loo loo) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", loo.getName()));
		params.add(new BasicNameValuePair("longitude", Double.toString(loo.getCoordinates()[0])));
		params.add(new BasicNameValuePair("latitude", Double.toString(loo.getCoordinates()[1])));
		params.add(new BasicNameValuePair("rating", Integer.toString(loo.getRating())));
		params.add(new BasicNameValuePair("operational", loo.getOperational().toString()));
		params.add(new BasicNameValuePair("hygienic", loo.getHygienic().toString()));
		params.add(new BasicNameValuePair("free", loo.getFree().toString()));
		params.add(new BasicNameValuePair("kind", loo.getType()));
		params.add(new BasicNameValuePair("suitableFor", loo.getSuitableFor()));
		return params;
	}
}
package com.thoughtworks.sulabh.gateways;

import com.thoughtworks.sulabh.model.Loo;
import com.thoughtworks.sulabh.model.LooList;
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
			String url = "http://router.shwetado.nodejitsu.com/locations?latitude=" + latitude + "&longitude=" + longitude;
			System.out.println("url = " + url);
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				String responseString = out.toString();
				ObjectMapper mapper = new ObjectMapper();
                System.out.println("*************** " + responseString);
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

	private boolean getResponse(Loo loo, String postURL) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			List<NameValuePair> params = getNameValuePairs(loo);

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
			post.setEntity(ent);
			HttpResponse responsePOST = client.execute(post);
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

	public boolean addLoo(Loo loo) {
		String url = "http://router.shwetado.nodejitsu.com/add";
		return getResponse(loo, url);
	}

	public boolean updateLoo(Loo loo) {
        String url = "http://router.shwetado.nodejitsu.com/update";
		return getResponse(loo, url);
	}

	public boolean rate(Loo loo) {
		String url = "http://router.shwetado.nodejitsu.com/rate";
		return getResponse(loo, url);
	}

	private List<NameValuePair> getNameValuePairs(Loo loo) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", loo.getName()));
		params.add(new BasicNameValuePair("latitude", Double.toString(loo.getCoordinates()[0])));
		params.add(new BasicNameValuePair("longitude", Double.toString(loo.getCoordinates()[1])));
		params.add(new BasicNameValuePair("rating", Float.toString(loo.getActualRating())));
		params.add(new BasicNameValuePair("operational", loo.getOperational().toString()));
		params.add(new BasicNameValuePair("free", loo.getFree().toString()));
		params.add(new BasicNameValuePair("kind", loo.getType()));
		params.add(new BasicNameValuePair("suitableFor", loo.getSuitableFor()));
		return params;
	}
}
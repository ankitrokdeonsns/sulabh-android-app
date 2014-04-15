package com.thoughtworks.sulabh;

import com.google.android.gms.maps.model.LatLng;

public class Loo {
	private String _id;
	private String name;
	private double coordinates[];
	private String operational;
	private String hygienic;
	private String paid;
	private String kind;
	private String compatibility;

	private Loo() {
	}

	public Loo(String name, LatLng latLng) {
		this.name = name;
		this.coordinates = new double[]{latLng.latitude, latLng.longitude};
	}

	public String getName() {
		return name;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public String getOperational() {
		return operational;
	}

	public String getHygienic() {
		return hygienic;
	}

	public String getPaid() {
		return paid;
	}

	public String getKind() {
		return kind;
	}

	public String getCompatibility() {
		return compatibility;
	}

	public String get_id() {
		return _id;
	}
}
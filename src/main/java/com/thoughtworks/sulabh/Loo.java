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

	public boolean isSamePositionAs(LatLng position) {
		return areDoublesEqual(this.getCoordinates()[0], position.latitude) && areDoublesEqual(this.getCoordinates()[1], position.longitude);
	}

	private boolean areDoublesEqual(double a, double b) {
		double delta = 0.0000005;
		return Math.abs(a - b) < delta;
	}

}
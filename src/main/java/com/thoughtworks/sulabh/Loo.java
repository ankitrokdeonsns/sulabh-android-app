package com.thoughtworks.sulabh;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Loo implements Serializable{
	private String _id;
	private String name;
	private double coordinates[];
	private int rating;
	private Boolean operational;
	private Boolean hygienic;
	private Boolean free;
	private String type;
	private String suitableFor[];

	private Loo() {}

	public Loo(String[] suitableFor, String type, Boolean free, Boolean hygienic, Boolean operational, int rating, double[] coordinates, String name) {

		this.suitableFor = suitableFor;
		this.type = type;
		this.free = free;
		this.hygienic = hygienic;
		this.operational = operational;
		this.rating = rating;
		this.coordinates = coordinates;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public int getRating() {
		return rating;
	}

	public Boolean getOperational() {
		return operational;
	}

	public Boolean getHygienic() {
		return hygienic;
	}

	public Boolean getFree() {
		return free;
	}

	public String getType() {
		return type;
	}

	public String getSuitableFor() {
		StringBuilder suitable = new StringBuilder();
		for (String s : suitableFor)
			suitable.append(" " + s);
		return suitable.toString();
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
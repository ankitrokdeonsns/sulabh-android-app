package com.thoughtworks.sulabh.model;

import java.io.Serializable;

public class Rating implements Serializable{
	float sumOfRatings;
	int numOfRatings;

	public Rating() {
		sumOfRatings = 0;
		numOfRatings = 0;
	}

	public Rating(float sumOfRatings, int numOfRatings) {
		this.sumOfRatings = sumOfRatings;
		this.numOfRatings = numOfRatings;
	}

	public float getSumOfRatings() {
		return sumOfRatings;
	}

	public int getNumOfRatings() {
		return numOfRatings;
	}
}

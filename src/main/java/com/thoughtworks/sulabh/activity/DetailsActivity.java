package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.R;
import com.thoughtworks.sulabh.Loo;

public class DetailsActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		Loo loo = (Loo) extras.getSerializable("Loo");

		String name = loo.getName();
		TextView placeName = (TextView) findViewById(R.id.placeName);
		placeName.setText(name);

		int rating = loo.getRating();
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setNumStars(rating);

		String isOperational = String.valueOf(loo.getOperational());
		TextView operational = (TextView) findViewById(R.id.isOperational);
		operational.setText(mapValues(isOperational));

		String isHygienic = String.valueOf(loo.getHygienic());
		TextView hygienic = (TextView) findViewById(R.id.isHygienic);
		hygienic.setText(mapValues(isHygienic));

		String isFree = String.valueOf(loo.getFree());
		TextView free = (TextView) findViewById(R.id.isFree);
		free.setText(mapValues(isFree));

		String ofKind = loo.getType();
		TextView kind = (TextView) findViewById(R.id.ofKind);
		kind.setText(ofKind);

		String isSuitableFor = loo.getSuitableFor();
		TextView suitable = (TextView) findViewById(R.id.suitableFor);
		suitable.setText(isSuitableFor);
	}

	public String mapValues(String field) {
		if(field.equals("true"))
			field = "Yes";
		else
			field = "No";
		return field;
	}
}
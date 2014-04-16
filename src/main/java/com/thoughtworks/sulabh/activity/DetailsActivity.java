package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.R;

public class DetailsActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		String name = extras.getString("Name");
		TextView placeName = (TextView) findViewById(R.id.placeName);
		placeName.setText(name);

		int rating = (extras.getInt("Rating"));
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setNumStars(rating);

		String isOperational = extras.getString("Operational");
		TextView operational = (TextView) findViewById(R.id.isOperational);
		operational.setText(mapValues(isOperational));

		String isHygienic = extras.getString("Hygienic");
		TextView hygienic = (TextView) findViewById(R.id.isHygienic);
		hygienic.setText(mapValues(isHygienic));

		String isFree = extras.getString("Free/Paid");
		TextView free = (TextView) findViewById(R.id.isFree);
		free.setText(mapValues(isFree));

		String ofKind = extras.getString("Kind");
		TextView kind = (TextView) findViewById(R.id.ofKind);
		kind.setText(ofKind);

		String isSuitableFor = extras.getString("Suitable For");
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
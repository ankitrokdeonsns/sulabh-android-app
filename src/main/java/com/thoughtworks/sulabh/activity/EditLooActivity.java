package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.R;
import com.thoughtworks.sulabh.Loo;

public class EditLooActivity extends Activity{
	private Loo loo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_loo);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		loo = (Loo) extras.getSerializable("Loo");

		String name = loo.getName();
		TextView placeName = (TextView) findViewById(R.id.addName);
		placeName.setText(name);

		int rating = loo.getRating();
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setNumStars(rating);

		RadioGroup operationalStatus = (RadioGroup) findViewById(R.id.isOperational);
		setStatus(operationalStatus);

		RadioGroup hygienicStatus = (RadioGroup) findViewById(R.id.isHygienic);
		if(loo.getHygienic())
			hygienicStatus.findViewById(R.id.hygienicYes).performClick();
		else
			hygienicStatus.findViewById(R.id.hygienicNo).performClick();

		RadioGroup isFreeStatus = (RadioGroup) findViewById(R.id.isFree);
		if(loo.getFree())
			isFreeStatus.findViewById(R.id.freeYes).performClick();
		else
			isFreeStatus.findViewById(R.id.freeNo).performClick();

	}

	private void setStatus(RadioGroup operationalStatus) {
		if (loo.getOperational())
			operationalStatus.findViewById(R.id.operationalYes).performClick();
		else
			operationalStatus.findViewById(R.id.operationalNo).performClick();
	}
}

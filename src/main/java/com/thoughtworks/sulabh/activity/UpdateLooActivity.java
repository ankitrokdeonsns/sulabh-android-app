package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.R;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

public class UpdateLooActivity extends Activity{
	private Loo loo;
	private TextView placeName;
	private RatingBar ratingBar;
	private RadioGroup operationalStatus;
	private RadioGroup hygienicStatus;
	private RadioGroup freeStatus;
	private Button submit;
	private RadioButton operational;
	private RadioButton hygienic;
	private RadioButton free;
	private Spinner kind;
	private Spinner suitableFor;
	private Loo newLoo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_loo);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		loo = (Loo) extras.getSerializable("Loo");

		String name = loo.getName();
		placeName = (TextView) findViewById(R.id.addName);
		placeName.setText(name);

		int rating = loo.getRating();
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setNumStars(rating);

		operationalStatus = (RadioGroup) findViewById(R.id.isOperational);
		setStatus(operationalStatus);

		hygienicStatus = (RadioGroup) findViewById(R.id.isHygienic);
		if(loo.getHygienic())
			hygienicStatus.findViewById(R.id.hygienicYes).performClick();
		else
			hygienicStatus.findViewById(R.id.hygienicNo).performClick();

		freeStatus = (RadioGroup) findViewById(R.id.isFree);
		if(loo.getFree())
			freeStatus.findViewById(R.id.freeYes).performClick();
		else
			freeStatus.findViewById(R.id.freeNo).performClick();

		kind = (Spinner) findViewById(R.id.type);
		suitableFor = (Spinner) findViewById(R.id.suitableTo);

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = String.valueOf(UpdateLooActivity.this.placeName.getText());
				int rating = (int) ratingBar.getRating();

				int selectedOperational = operationalStatus.getCheckedRadioButtonId();
				operational = (RadioButton) operationalStatus.findViewById(selectedOperational);
				boolean isOperationalChecked = operational.isChecked();

				int selectedHygienic = hygienicStatus.getCheckedRadioButtonId();
				hygienic = (RadioButton) hygienicStatus.findViewById(selectedHygienic);
				boolean isHygienicChecked = hygienic.isChecked();

				int selectedFree = freeStatus.getCheckedRadioButtonId();
				free = (RadioButton) freeStatus.findViewById(selectedFree);
				boolean isFreeChecked = free.isChecked();

				String kind = UpdateLooActivity.this.kind.getSelectedItem().toString();
				String suitableFor = UpdateLooActivity.this.suitableFor.getSelectedItem().toString();
				double[] location = {loo.getCoordinates()[0], loo.getCoordinates()[1]};

				newLoo = new Loo(new String[]{suitableFor}, kind, isFreeChecked, isHygienicChecked, isOperationalChecked, rating, location, name);

				boolean isAdded = new SulabhGateway().updateLoo(newLoo);
				if (isAdded) {
					Intent intent = new Intent(UpdateLooActivity.this, LaunchActivity.class);
					intent.putExtra("toastMessage", "Updated Successfully");
					intent.putExtra("isPressed", true);
					startActivity(intent);
				}
			}
		});
	}

	private void setStatus(RadioGroup operationalStatus) {
		if (loo.getOperational())
			operationalStatus.findViewById(R.id.operationalYes).performClick();
		else
			operationalStatus.findViewById(R.id.operationalNo).performClick();
	}
}
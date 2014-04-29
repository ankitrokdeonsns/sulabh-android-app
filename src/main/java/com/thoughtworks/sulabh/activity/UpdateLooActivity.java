package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.R;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.LooDetailsPopup;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

import java.util.ArrayList;

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
	private Button suitableFor;
	private Loo newLoo;
    private LooDetailsPopup looDetailsPopup = new LooDetailsPopup(this);
    private CharSequence[] suitableOptions = { "Men", "Women", "Babies", "TransGender", "Handicapped"};
    protected ArrayList<CharSequence> selectedCategories = new ArrayList<CharSequence>();
    private String name;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_loo);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		loo = (Loo) extras.getSerializable("Loo");

		String previousName = loo.getName();
		placeName = (TextView) findViewById(R.id.addName);
		placeName.setText(previousName);

		float rating = loo.getActualRating();
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setRating(rating);

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
		suitableFor = (Button) findViewById(R.id.suitableTo);

        suitableFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                looDetailsPopup.showSelectCategoriesDialog();
            }
        });

		submit = (Button) findViewById(R.id.submit);
		submit.setText("Update");
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                int selectedOperational = operationalStatus.getCheckedRadioButtonId();
                operational = (RadioButton) operationalStatus.findViewById(selectedOperational);
                int selectedHygienic = hygienicStatus.getCheckedRadioButtonId();
                hygienic = (RadioButton) hygienicStatus.findViewById(selectedHygienic);
                int selectedFree = freeStatus.getCheckedRadioButtonId();
                free = (RadioButton) freeStatus.findViewById(selectedFree);

                if (!isDataValid())
                    Toast.makeText(getApplicationContext(),"All fields are mandatory!",Toast.LENGTH_LONG).show();
                else {
                    name = String.valueOf(UpdateLooActivity.this.placeName.getText());
                    float rating = ratingBar.getRating();
                    boolean isOperationalChecked = operational.isChecked();
                    boolean isHygienicChecked = hygienic.isChecked();
                    boolean isFreeChecked = free.isChecked();

                    String kind = UpdateLooActivity.this.kind.getSelectedItem().toString();
                    String suitableFor = UpdateLooActivity.this.suitableFor.getText().toString();
                    String[] suitableCategories = suitableFor.split(",");
                    double[] location = {loo.getCoordinates()[0], loo.getCoordinates()[1]};
                    newLoo = new Loo(suitableCategories, kind, isFreeChecked, isHygienicChecked, isOperationalChecked, rating, location, name);

                    boolean isAdded = new SulabhGateway().updateLoo(newLoo);
                    if (isAdded) {
                        Intent intent = new Intent(UpdateLooActivity.this, LaunchActivity.class);
                        intent.putExtra("toastMessage", "Updated Successfully");
                        intent.putExtra("isPressed", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
			}
		});
	}

    private boolean isDataValid(){
        if(!placeName.getText().toString().trim().equals("") &&
                operational.isChecked() &&
                hygienic.isChecked() &&
                free.isChecked() &&
                !kind.getSelectedItem().toString().equals("") &&
                !suitableFor.getText().toString().equals("None selected !"))
            return true;
        return false;
    }

    private void setStatus(RadioGroup operationalStatus) {
		if (loo.getOperational())
			operationalStatus.findViewById(R.id.operationalYes).performClick();
		else
			operationalStatus.findViewById(R.id.operationalNo).performClick();
	}

    public CharSequence[] getSuitableOptions() {
        return suitableOptions;
    }

    public ArrayList<CharSequence> getSelectedCategories() {
        return selectedCategories;
    }

    public Button getSuitableFor() {
        return suitableFor;
    }
}

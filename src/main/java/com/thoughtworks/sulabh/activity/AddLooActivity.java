package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.R;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

import java.util.ArrayList;

public class AddLooActivity extends Activity{

    private final LooDetailsPopup looDetailsPopup = new LooDetailsPopup(this);
    private Loo newLoo;
    private EditText name;
    private RatingBar ratingBar;
    private Spinner kind;
    private Button suitableFor;
    private RadioButton operational;
    private RadioButton free;
    private RadioButton hygienic;
    private CharSequence[] suitableOptions = { "Men", "Women", "Babies", "TransGender", "Handicapped"};
    protected ArrayList<CharSequence> selectedCategories = new ArrayList<CharSequence>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_loo);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final double[] coordinates = extras.getDoubleArray("coordinates");
        name = (EditText) findViewById(R.id.addName);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        final RadioGroup isOperational = (RadioGroup) findViewById(R.id.isOperational);
        final RadioGroup isHygienic = (RadioGroup) findViewById(R.id.isHygienic);
        final RadioGroup isFree = (RadioGroup) findViewById(R.id.isFree);

        kind = (Spinner) findViewById(R.id.type);
        suitableFor = (Button) findViewById(R.id.suitableTo);
        suitableFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                looDetailsPopup.showSelectCategoriesDialog();
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(AddLooActivity.this.name.getText());
                float rating = (float) ratingBar.getRating();

                int selectedOperational = isOperational.getCheckedRadioButtonId();
                operational = (RadioButton) isOperational.findViewById(selectedOperational);
                boolean isOperationalChecked = operational.isChecked();

                int selectedHygienic = isHygienic.getCheckedRadioButtonId();
                hygienic = (RadioButton) isHygienic.findViewById(selectedHygienic);
                boolean isHygienicChecked = hygienic.isChecked();

                int selectedFree = isFree.getCheckedRadioButtonId();
                free = (RadioButton) isFree.findViewById(selectedFree);
                boolean isFreeChecked = free.isChecked();

                String kind = AddLooActivity.this.kind.getSelectedItem().toString();
                String suitableFor = AddLooActivity.this.suitableFor.getText().toString();
                String[] suitableCategories = suitableFor.split(",");
                double[] location = {coordinates[0], coordinates[1]};

                newLoo = new Loo(suitableCategories, kind, isFreeChecked, isHygienicChecked, isOperationalChecked, rating, location, name);

                boolean isAdded = new SulabhGateway().addLoo(newLoo);
                if (isAdded) {
                    Intent intent = new Intent(AddLooActivity.this, LaunchActivity.class);
                    intent.putExtra("toastMessage", "Added Successfully");
                    intent.putExtra("isPressed",true);
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public ArrayList<CharSequence> getSelectedCategories() {
        return selectedCategories;
    }

    public Button getSuitableFor() {
        return suitableFor;
    }

    public CharSequence[] getSuitableOptions() {
        return suitableOptions;
    }
}
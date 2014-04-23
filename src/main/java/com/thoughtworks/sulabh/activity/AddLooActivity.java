package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.R;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

import java.util.ArrayList;

public class AddLooActivity extends Activity{

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
                showSelectCategoriesDialog();
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(AddLooActivity.this.name.getText());
                int rating = (int) ratingBar.getRating();

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
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    protected void onChangeSelectedCategories() {
        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence category : selectedCategories)
            stringBuilder.append(category + ",");

        suitableFor.setText(stringBuilder.toString());
    }

    protected void showSelectCategoriesDialog() {
        boolean[] checkedCategories = new boolean[suitableOptions.length];
        int count = suitableOptions.length;

        for(int i = 0; i < count; i++)
            checkedCategories[i] = selectedCategories.contains(suitableOptions[i]);

        DialogInterface.OnMultiChoiceClickListener categoriesDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                    selectedCategories.add(suitableOptions[which]);
                else
                    selectedCategories.remove(suitableOptions[which]);

                onChangeSelectedCategories();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Category");
        builder.setMultiChoiceItems(suitableOptions, checkedCategories, categoriesDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
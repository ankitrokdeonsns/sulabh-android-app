package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.thoughtworks.R;
import com.thoughtworks.sulabh.handler.AddResponseHandler;
import com.thoughtworks.sulabh.helper.AddCallback;
import com.thoughtworks.sulabh.helper.KeyboardHelper;
import com.thoughtworks.sulabh.model.Loo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddLooActivity extends Activity{

	private Loo newLoo;
	private EditText name;
	private RatingBar ratingBar;
	private Spinner kind;
	private RadioButton operational;
	private RadioButton free;
	private CheckBox men;
	private CheckBox women;
	private CheckBox babies;
	private CheckBox transGender;
	private CheckBox handicapped;
    private double[] coordinates;
    private RadioGroup isOperational;
    private RadioGroup isFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_loo);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        coordinates = extras.getDoubleArray("coordinates");
        name = (EditText) findViewById(R.id.addName);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        isOperational = (RadioGroup) findViewById(R.id.isOperational);
        isFree = (RadioGroup) findViewById(R.id.isFree);

        isOperational.check(R.id.operationalYes);
        isFree.check(R.id.freeYes);

        kind = (Spinner) findViewById(R.id.type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Save")){
            int selectedOperational = isOperational.getCheckedRadioButtonId();
            operational = (RadioButton) isOperational.findViewById(selectedOperational);
            int selectedFree = isFree.getCheckedRadioButtonId();
            free = (RadioButton) isFree.findViewById(selectedFree);

            men = (CheckBox) findViewById(R.id.men);
            women = (CheckBox) findViewById(R.id.women);
            babies = (CheckBox) findViewById(R.id.babies);
            transGender = (CheckBox) findViewById(R.id.transgender);
            handicapped = (CheckBox) findViewById(R.id.handicapped);

            if (!isDataValid()) {
                Toast.makeText(getApplicationContext(), "All fields are mandatory!", Toast.LENGTH_LONG).show();
            }
            else {
                addLoo();
            }
            return true;
        }
        finish();
        return true;
    }

    private void addLoo() {
        String name = String.valueOf(AddLooActivity.this.name.getText());
        float rating = ratingBar.getRating();
        boolean isOperationalChecked = true;
        boolean isFreeChecked = true;

        if(operational.getText().equals("No")) isOperationalChecked = false;
        if(free.getText().equals("No")) isFreeChecked = false;

        String kind = AddLooActivity.this.kind.getSelectedItem().toString();

        List<CheckBox> suitableForValues = new ArrayList<CheckBox>();

        suitableForValues.add(men);
        suitableForValues.add(women);
        suitableForValues.add(babies);
        suitableForValues.add(transGender);
        suitableForValues.add(handicapped);

        StringBuilder values = new StringBuilder();
        for (CheckBox suitableForValue : suitableForValues) {
            if(suitableForValue.isChecked())
                values.append(suitableForValue.getText()).append("\n");
        }

        String[] suitableCategories = values.toString().split("\n");

        double[] location = {coordinates[0], coordinates[1]};
        newLoo = new Loo(suitableCategories, kind, isFreeChecked, isOperationalChecked, rating, location, name);
        new AddResponseHandler(callback(), newLoo).execute();
    }


    private boolean isDataValid(){
        return !name.getText().toString().trim().equals("") &&
                (men.isChecked() || women.isChecked() ||babies.isChecked() || transGender.isChecked() ||handicapped.isChecked());
    }

    private AddCallback<Boolean> callback(){
        return new AddCallback<Boolean>() {
            @Override
            public void execute(Boolean isAdded) throws IOException {
                if (isAdded) {
                    Intent intent = new Intent(AddLooActivity.this, LaunchActivity.class);
                    intent.putExtra("toastMessage", "Added Successfully");
                    intent.putExtra("isPressed", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean result = super.dispatchTouchEvent(event);
        new KeyboardHelper().dismissKeyboard(view,event,this);
        return result;
    }
}
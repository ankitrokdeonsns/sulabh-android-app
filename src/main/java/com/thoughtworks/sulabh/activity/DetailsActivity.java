package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.thoughtworks.R;
import com.thoughtworks.sulabh.model.Loo;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity{

	private Loo loo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		loo = (Loo) extras.getSerializable("Loo");

		String name = loo.getName();
		TextView placeName = (TextView) findViewById(R.id.placeName);
		placeName.setText(name);

		float rating = loo.getActualRating();
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setRating(rating);

		String isOperational = String.valueOf(loo.getOperational());
		TextView operational = (TextView) findViewById(R.id.isOperational);
		operational.setText(mapValues(isOperational));

		String isFree = String.valueOf(loo.getFree());
		TextView free = (TextView) findViewById(R.id.isFree);
		free.setText(mapValues(isFree));

		String ofKind = loo.getType();
		TextView kind = (TextView) findViewById(R.id.ofKind);
		kind.setText(ofKind);

		String isSuitableFor = loo.getSuitableFor();
		String[] suitableOptions = isSuitableFor.split("\n");

        mapImages(suitableOptions);
	}

    private void mapImages(String[] suitableOptions) {
        ImageView men = (ImageView) findViewById(R.id.menIcon);
        men.setImageDrawable(null);
        ImageView women = (ImageView) findViewById(R.id.womenIcon);
        women.setImageDrawable(null);
        ImageView babies = (ImageView) findViewById(R.id.babiesIcon);
        babies.setImageDrawable(null);
        ImageView handicapped = (ImageView) findViewById(R.id.handicappedIcon);
        handicapped.setImageDrawable(null);
        ImageView transGender = (ImageView) findViewById(R.id.transgenderIcon);
        transGender.setImageDrawable(null);

        for (String suitable : suitableOptions) {
            if (suitable.equals("Men")) men.setImageResource(R.drawable.men);
            if (suitable.equals("Women")) women.setImageResource(R.drawable.women);
            if (suitable.equals("Babies")) babies.setImageResource(R.drawable.babies);
            if (suitable.equals("Handicapped")) handicapped.setImageResource(R.drawable.handicapped);
            if (suitable.equals("TransGender")) transGender.setImageResource(R.drawable.transgender);
        }

        List<ImageView> suits = new ArrayList<ImageView>();
        suits.add(men);
        suits.add(women);
        suits.add(babies);
        suits.add(handicapped);
        suits.add(transGender);

        for (ImageView suit : suits) {
            if(suit.getDrawable() == null)
                suit.setVisibility(suit.GONE);
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().equals("edit")) {
			Intent intent = new Intent(DetailsActivity.this, UpdateLooActivity.class);
			intent.putExtra("Loo", loo);
			startActivity(intent);
			return true;
		}
		Intent intent = new Intent(DetailsActivity.this,RatingActivity.class);
		intent.putExtra("Loo", loo);
		startActivity(intent);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return true;
	}

	public String mapValues(String field) {
		if(field.equals("true"))
			field = "Yes";
		else
			field = "No";
		return field;
	}

}
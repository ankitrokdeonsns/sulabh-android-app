package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import com.example.R;
import com.thoughtworks.sulabh.Loo;
import com.thoughtworks.sulabh.Rating;
import com.thoughtworks.sulabh.gateways.SulabhGateway;

public class RatingActivity extends Activity {

	private Loo loo;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingbar);

	    Intent intent = getIntent();
	    Bundle extras = intent.getExtras();

		loo = (Loo) extras.getSerializable("Loo");
	    Button submitRating = (Button) findViewById(R.id.popupSubmit);

	    submitRating.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    final RatingBar ratingBar = (RatingBar)findViewById(R.id.popupRating);
			    float rating = ratingBar.getRating();
			    Rating updatedRating = new Rating(rating, 1);
			    loo.setRating(updatedRating);

				Loo newLoo = new Loo(new String[]{loo.getSuitableFor()}, loo.getType(), loo.getFree(),
						loo.getHygienic(), loo.getOperational(), rating, loo.getCoordinates(), loo.getName());

			    new SulabhGateway().rate(newLoo);
			    Intent intent = new Intent(RatingActivity.this, LaunchActivity.class);
			    intent.putExtra("Loo", newLoo);
			    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    Toast.makeText(getApplicationContext(), "Rated Successfully", Toast.LENGTH_LONG).show();
			    startActivity(intent);
			    finish();
		    }
	    });
    }
}
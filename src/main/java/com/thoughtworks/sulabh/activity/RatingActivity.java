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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingbar);

	    Intent intent = getIntent();
	    Bundle extras = intent.getExtras();

	    final Loo loo = (Loo) extras.getSerializable("Loo");
	    final RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);

	    float rating = ratingBar.getRating();
	    float sumOfRatings = loo.getRating().getSumOfRatings() + rating;
	    int numOfRatings = loo.getRating().getNumOfRatings() + 1;

	    Rating updatedRating = new Rating(sumOfRatings, numOfRatings);
	    loo.setRating(updatedRating);

	    Button submitRating = (Button) findViewById(R.id.submit);

	    submitRating.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    new SulabhGateway().rate(loo);
			    Intent intent = new Intent(RatingActivity.this, DetailsActivity.class);
			    intent.putExtra("Loo",loo);
			    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    Toast.makeText(getApplicationContext(), "Rated Successfully", Toast.LENGTH_LONG).show();
			    startActivity(intent);
			    finish();
		    }
	    });
    }
}
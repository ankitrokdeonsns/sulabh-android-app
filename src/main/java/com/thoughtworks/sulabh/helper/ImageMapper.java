package com.thoughtworks.sulabh.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.thoughtworks.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.thoughtworks.sulabh.activity.LaunchActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageMapper {
	private final LaunchActivity launchActivity;

	public ImageMapper(LaunchActivity launchActivity) {
		this.launchActivity = launchActivity;
	}

	public void mapImages(){
		launchActivity.getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			private final View contents = launchActivity.getLayoutInflater().inflate(R.layout.marker_details, null);

			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				TextView txtTitle = (TextView) contents.findViewById(R.id.looName);
				RatingBar ratingBar = (RatingBar) contents.findViewById(R.id.markerRatingBar);

				String suitableFor = launchActivity.getSelectedLoo().getSuitableFor();
				String[] suitableOptions = suitableFor.split("\n");

				ImageView men = (ImageView) contents.findViewById(R.id.men);
				men.setImageDrawable(null);
				ImageView women = (ImageView) contents.findViewById(R.id.women);
				women.setImageDrawable(null);
				ImageView babies = (ImageView) contents.findViewById(R.id.babies);
				babies.setImageDrawable(null);
				ImageView handicapped = (ImageView) contents.findViewById(R.id.handicapped);
				handicapped.setImageDrawable(null);
				ImageView transGender = (ImageView) contents.findViewById(R.id.transgender);
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

				txtTitle.setText(launchActivity.getSelectedLoo().getName());
				ratingBar.setRating(launchActivity.getSelectedLoo().getActualRating());
				return contents;
			}
		});
	}
}
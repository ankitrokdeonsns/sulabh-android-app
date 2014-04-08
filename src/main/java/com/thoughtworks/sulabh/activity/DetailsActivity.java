package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.R;

public class DetailsActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String operational = extras.getString("Operational");
        String hygienic = extras.getString("Hygienic");
        String free = extras.getString("Free/Paid");
        String kind = extras.getString("Kind");
        String suitable = extras.getString("Suitable For");
        TextView view = (TextView) findViewById(R.id.placeName);
        TextView view1 = (TextView) findViewById(R.id.isOperational);
        view1.setText(operational);
        TextView view2 = (TextView) findViewById(R.id.isHygienic);
        view2.setText(hygienic);
        TextView view3 = (TextView) findViewById(R.id.isFree);
        view3.setText(free);
        TextView view4 = (TextView) findViewById(R.id.ofKind);
        view4.setText(kind);
        TextView view5 = (TextView) findViewById(R.id.suitableFor);
        view5.setText(suitable);

        view.setText("You are here");
    }
}

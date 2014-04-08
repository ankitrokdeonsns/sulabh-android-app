package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.R;

public class DetailsActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        TextView view = (TextView) findViewById(R.id.placeName);
        view.setText("You are here");
    }
}

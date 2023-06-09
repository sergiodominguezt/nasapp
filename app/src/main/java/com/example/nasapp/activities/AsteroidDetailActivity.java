package com.example.nasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nasapp.R;



public class AsteroidDetailActivity extends AppCompatActivity {
    private Toolbar toolbarDetailed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asteroid_detail);

        toolbarDetailed = findViewById(R.id.idToolbarDetailed);
        setSupportActionBar(toolbarDetailed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        displayDetailedAsteroid();

    }
    private void displayDetailedAsteroid() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String nasaJplUrl = intent.getStringExtra("nasaJplUrl");
        String hazardousAsteroid = intent.getStringExtra("hazardousAsteroid");
        String isSentryObject = intent.getStringExtra("isSentryObject");
        String absoluteMagnitude = intent.getStringExtra("absoluteMagnitude");
        String neo_reference_id = intent.getStringExtra("neoReferenceId");
        TextView nameAsteroidView = findViewById(R.id.asteroidNameDetail);
        TextView nasaJplUrlView = findViewById(R.id.asteroidNasaJplUrlDetail);
        TextView hazardousAsteroidView = findViewById(R.id.asteroidDangerousDetail);
        TextView isSentryObjectView = findViewById(R.id.asteroidIsSentryDetail);
        TextView absoluteMagnitudeView = findViewById(R.id.asteroidAbsoluteDetail);
        TextView neoReferenceIdView = findViewById(R.id.asteroidNeoIdDetail);
        nameAsteroidView.setText(name);
        nasaJplUrlView.setText(nasaJplUrl);
        neoReferenceIdView.setText(neo_reference_id);
        hazardousAsteroidView.setText(hazardousAsteroid);
        isSentryObjectView.setText(isSentryObject);
        absoluteMagnitudeView.setText(absoluteMagnitude);

    }

}

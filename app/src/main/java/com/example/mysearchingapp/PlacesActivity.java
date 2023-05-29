package com.example.mysearchingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mysearchingapp.util.Util;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Places.initialize(getApplicationContext(), "api_key_here");
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Util.createToast(getApplicationContext(), "Error with API Key.");
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                String placeName = place.getName();

                //Check request code
                Intent checkIntent = getIntent();
                int requestCode = checkIntent.getIntExtra("requestCode", 0);
                Intent locationIntent = new Intent(getApplicationContext(), DeliveryActivity.class);

                if (requestCode == Util.PICK_DESTINATION_REQUEST)
                {
                    locationIntent.putExtra(Util.DESTINATION_LATITUDE, latitude);
                    locationIntent.putExtra(Util.DESTINATION_LONGITUDE, longitude);
                    locationIntent.putExtra(Util.DESTINATION, placeName);
                    setResult(RESULT_OK, locationIntent);
                    finish();
                }
                else
                {
                    setResult(RESULT_CANCELED, locationIntent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

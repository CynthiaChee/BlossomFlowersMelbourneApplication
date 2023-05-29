package com.example.mysearchingapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mysearchingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Initializing variables
    private GoogleMap googleMap;
    DirectionsResult directionsResult;
    String shopLocation, destinationLocation;
    double destLat, destLong, cost;
    TextView shopText, destText, costText;
    Button bookButton, callButton;
    private ActivityMapsBinding binding;
    private static final int REQUEST_PHONE_CALL = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("client_id_here");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Find view by ID
        shopText = findViewById(R.id.shopLocationText);
        destText = findViewById(R.id.destinationLocationText);
        costText = findViewById(R.id.costText);
        bookButton = findViewById(R.id.bookButton);
        callButton = findViewById(R.id.callButton);

        //Get data from previous intent
        shopLocation = "Blossom Flowers Melbourne";
        destinationLocation = getIntent().getStringExtra("destination");
        destLat = getIntent().getDoubleExtra("destinationLatitude",0);
        destLong = getIntent().getDoubleExtra("destinationLongitude",0);
        shopText.setText("Delivering from: "+ shopLocation);
        destText.setText("Destination: "+ destinationLocation);

        //If book button is clicked
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(cost), "AUD", "Fee",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent paymentIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivity(paymentIntent);
            }
        });

        //If call button is clicked
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0123456789"));

                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(callIntent);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //Shop address is fixed
        LatLng shopAddress = new LatLng(-37.8168885,144.9665602);
        LatLng destLatLng = new LatLng(destLat, destLong);

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
            .apiKey("api_key_here")
            .build();

        try {
            directionsResult = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(shopAddress.latitude, shopAddress.longitude))
                    .destination(new com.google.maps.model.LatLng(destLatLng.latitude, destLatLng.longitude))
                    .transitMode()
                    .await();
            List<LatLng> decodedPath = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.getEncodedPath());
            this.googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cost = 50.20;
        String costText = String.valueOf(cost);
        this.costText.setText("Approx. Cost: $" + costText);

        //Add markers to map
        this.googleMap.addMarker(new MarkerOptions().position(shopAddress));
        this.googleMap.addMarker(new MarkerOptions().position(destLatLng));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopAddress,15));
    }

}


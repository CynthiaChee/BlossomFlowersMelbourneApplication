package com.example.mysearchingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mysearchingapp.util.Util;

public class OrderFragment extends Fragment {

    //Initializing variables
    private String receiverName, date, time, destination, flowerType, quantity, message;
    private double destinationLatitude, destinationLongitude;
    Bitmap flowerImageBitmap;
    byte[] flowerImageBytesArray;

    public OrderFragment(Order order) {

        this.receiverName = order.getReceiverName();
        this.time = order.getTime();
        this.date = order.getDate();
        this.destination = order.getDestination();
        this.destinationLongitude = order.getDestinationLongitude();
        this.destinationLatitude = order.getDestinationLatitude();
        this.flowerType = order.getFlowerType();
        this.quantity = order.getQuantity();
        this.message = order.getMessage();
        this.flowerImageBytesArray = order.getFlowerImageBytes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowerImageBitmap = Util.getBitmapFromBytesArray(flowerImageBytesArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView fragmentGoodImageView = view.findViewById(R.id.orderImage);
        TextView receiverNameTextView = view.findViewById(R.id.fragmentReceiverData);
        TextView dateTextView = view.findViewById(R.id.dateFragmentTV);
        TextView timeTextView = view.findViewById(R.id.fragmentTimeData);
        TextView destinationTextView = view.findViewById(R.id.fragmentDestinationData);
        TextView quantityTextView = view.findViewById(R.id.qtyFragmentTV);
        TextView messageTextView = view.findViewById(R.id.msgFragmentTV);
        Button estimateButton = view.findViewById(R.id.getEstimateButton);

        //If get estimate button is clicked
        estimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
                mapIntent.putExtra(Util.RECEIVER_NAME, receiverName);
                mapIntent.putExtra(Util.DATE, date);
                mapIntent.putExtra(Util.TIME, time);
                mapIntent.putExtra(Util.FLOWER_TYPE, flowerType);
                mapIntent.putExtra(Util.QUANTITY, quantity);
                mapIntent.putExtra(Util.MSG, message);
                mapIntent.putExtra(Util.DESTINATION_LATITUDE, destinationLatitude);
                mapIntent.putExtra(Util.DESTINATION_LONGITUDE, destinationLongitude);
                mapIntent.putExtra(Util.DESTINATION, destination);
                startActivity(mapIntent);
            }
        });

        fragmentGoodImageView.setImageBitmap(flowerImageBitmap);
        receiverNameTextView.setText(receiverName);
        dateTextView.setText(date);
        timeTextView.setText(time);
        dateTextView.setText(date);
        destinationTextView.setText(destination);
        quantityTextView.setText(quantity);
        messageTextView.setText(message);
    }
}

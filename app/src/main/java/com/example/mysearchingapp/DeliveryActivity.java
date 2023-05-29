package com.example.mysearchingapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysearchingapp.util.Util;

public class DeliveryActivity extends AppCompatActivity {

    //Initializing variables
    String format, receiverName, deliveryDate, deliveryTime, destination;
    int hour, minute;
    double destinationLatitude, destinationLongitude;
    CalendarView myCalendarView;
    EditText receiverNameEditText, destinationEditText;
    TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //Find view by ID
        receiverNameEditText = findViewById(R.id.receiverEdit);
        destinationEditText = findViewById(R.id.destinationEdit);
        timeTextView = findViewById(R.id.timeTextView);
        myCalendarView = findViewById(R.id.datePicker);

        //Get date
        myCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                deliveryDate = date + "/" + (month+1) + "/" + year;
            }
        });
    }

    //Get time
    public void popTimePicker(View view)
    {
        // listener if user clicks on a time slot
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;

                // constructing time string
                if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                StringBuilder time = new StringBuilder();
                time.append(String.format("%02d:%02d", hour, minute)).append(" ").append(format);

                deliveryTime = time.toString();
                timeTextView.setText(deliveryTime);
            }
        };

        //Clock view
        TimePickerDialog myTimePicker = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        myTimePicker.show();
        }

    //If destination button is clicked
    public void destinationClick(View view)
    {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra("requestCode", Util.PICK_DESTINATION_REQUEST);
        startActivityForResult(intent, Util.PICK_DESTINATION_REQUEST);
    }

    //Get results from Places autocomplete
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Util.PICK_DESTINATION_REQUEST && resultCode == RESULT_OK)
        {
            destination = data.getStringExtra(Util.DESTINATION);
            destinationEditText.setText(destination);
            destinationLatitude = data.getDoubleExtra(Util.DESTINATION_LATITUDE, 0);
            destinationLongitude = data.getDoubleExtra(Util.DESTINATION_LONGITUDE, 0);
        }
    }

    //If next button is clicked
    public void nextClick(View view)
    {
        receiverName = receiverNameEditText.getText().toString();

        //If any required fields are empty
        if (receiverName == "" || deliveryDate == null || deliveryTime == null || destination == "")
        {
            Util.createToast(getApplicationContext(), "Please fill in all required fields!");
        }
        else
        {
            Intent deliveryIntent = new Intent(DeliveryActivity.this, DeliveryFlowersMLActivity.class);
            deliveryIntent.putExtra(Util.RECEIVER_NAME, receiverName);
            deliveryIntent.putExtra(Util.DATE, deliveryDate);
            deliveryIntent.putExtra(Util.TIME, deliveryTime);
            deliveryIntent.putExtra(Util.DESTINATION, destination);
            deliveryIntent.putExtra(Util.DESTINATION_LATITUDE, destinationLatitude);
            deliveryIntent.putExtra(Util.DESTINATION_LONGITUDE, destinationLongitude);
            startActivity(deliveryIntent);
            finish();
        }
    }
    }

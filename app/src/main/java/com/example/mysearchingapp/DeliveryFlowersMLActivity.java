package com.example.mysearchingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysearchingapp.ml.FlowerModel;
import com.example.mysearchingapp.util.Util;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DeliveryFlowersMLActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Initializing variables
    String selectedFlower, selectedVehicle, username, quantity, message, flowerTypeML;
    double flowerTypeAccuracyML;
    ImageView flowerImage;
    TextView accuracyText, accuracyPct;
    EditText qtyEdit, messageEdit;
    Spinner flowerSpinner;
    Order myOrder;
    OrderDatabaseHelper myOrderDBHelper;
    public static final int IMAGE_GALLERY_REQUEST = 100;
    private Bitmap image;
    byte[] flowerImageBytes;

    //Adapter for Spinner
    ArrayAdapter<CharSequence> flowerTypeAdapter;

    //Machine Learning classification labels
    int imageSize = 224;
    String[] classes = {"Apple", "Apricot", "Arjun", "Basil", "Blueberry", "Cherry", "Chinar", "Corn", "Cranberry", "Daisy", "Dandelion", "Gauva",
        "Grape", "Lemon", "Mango", "Peach", "Pear", "Pepper", "Pomegranate", "Potato", "Raspberry", "Rice", "Rose", "Soybean", "Strawberry",
        "Sunflower", "Tomato", "Tulip", "Walnut"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_flowers_ml);

        myOrderDBHelper = new OrderDatabaseHelper(this);

        //Find view by ID
        flowerSpinner = findViewById(R.id.flowerTypeSpinner);
        flowerImage = findViewById(R.id.flowerImage);
        qtyEdit = findViewById(R.id.qtyEdit);
        messageEdit = findViewById(R.id.messageEdit);
        accuracyText = findViewById(R.id.accText);
        accuracyPct = findViewById(R.id.accPercent);

        //Applying adapter to Spinner
        flowerTypeAdapter = ArrayAdapter.createFromResource(this, R.array.flowers, android.R.layout.simple_spinner_item);
        flowerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flowerSpinner.setAdapter(flowerTypeAdapter);
        flowerSpinner.setOnItemSelectedListener(this);
    }

    //If create order button is clicked
    public void createOrderClick(View view)
    {
        //Set default image if no image chosen
        if (flowerImageBytes == null)
        {
            setDefaultImage();
        }
        else
        {
            //Get data from previous activity
            Intent intent = getIntent();
            String receiverName = intent.getStringExtra(Util.RECEIVER_NAME);
            String date = intent.getStringExtra(Util.DATE);
            String time = intent.getStringExtra(Util.TIME);
            String destination = intent.getStringExtra(Util.DESTINATION);
            double destinationLatitude = intent.getDoubleExtra(Util.DESTINATION_LATITUDE, 0);
            double destinationLongitude = intent.getDoubleExtra(Util.DESTINATION_LONGITUDE, 0);

            //Get user input
            message = messageEdit.getText().toString();
            quantity = qtyEdit.getText().toString();

            //Get current logged in user
            SharedPreferences prefs = getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
            username = prefs.getString(Util.LOGGEDIN_USER, "");

            //Create new order and insert into database
            myOrder = new Order(username, flowerImageBytes, receiverName, date, time, destination, destinationLatitude, destinationLongitude, selectedFlower, quantity, message);
            long rowID = myOrderDBHelper.insertOrder(myOrder);

            if (rowID > 0) {
                Util.createToast(getApplicationContext(), "Order Successful!");
                Intent orderIntent = new Intent(this, OrdersActivity.class);
                startActivity(orderIntent);
                finish();
            }
            else {
                Util.createToast(getApplicationContext(), "Error: Please try again.");
            }
        }
    }

    //Set default image if none chosen
    public void setDefaultImage()
    {
        Bitmap defaultPic = BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
        flowerImageBytes = Util.getBytesArrayFromBitmap(defaultPic);
        flowerImage.setImageBitmap(defaultPic);
    }

    //Select image
    public void popPhotoPicker(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
    }

    //Results from gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {

                Uri imageUri = data.getData();
                InputStream inputStream;

                try {

                    inputStream = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(inputStream);

                    //Scale image
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    flowerImageBytes = Util.getBytesArrayFromBitmap(image);
                    flowerImage.setImageBitmap(image);

                    //Classify image using Machine Learning
                    classifyImage(image);

                }
                //If file not found
                catch (FileNotFoundException e) {
                    Util.createToast(this, "File not found!");
                }
            }
            //If no image selected, set to default image
            else {
                setDefaultImage();
            }
        }
    }

    //If item selected in spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.flowerTypeSpinner)
        {
            selectedFlower = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO override method if no selection was made on the spinner
    }

    //Image classification using Machine Learning
    @SuppressLint("SetTextI18n")
    public void classifyImage(Bitmap image) {
        try {
            //Create ML model
            FlowerModel model = FlowerModel.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            //Get pixels for image
            int [] intValues = new int[imageSize * imageSize]; //224 * 224
            image.getPixels(intValues, 0, image.getWidth(), 0 , 0, image.getWidth(), image.getHeight());

            int pixel = 0;

            //Image pixels in horizontal axis
            for (int i = 0; i < imageSize; i++) {
                //Image pixels in vertical axis
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    //Extract RGB of each pixel
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 0xFF) * (1.f / 255.f)));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            //Process input
            FlowerModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getProbabilityAsTensorBuffer();

            //Confidence variables
            float[] accuracy  = outputFeature0.getFloatArray();
            int maxPos = 0; // position of maximum accuracy
            float maxAccuracy = 0; // initialises maximum accuracy

            //Get the maximum accuracy of all the results
            for (int i = 0; i < accuracy.length; i++) {
                if (accuracy[i] > maxAccuracy) {
                    maxAccuracy = accuracy[i];
                    maxPos = i;
                }
            }

            flowerTypeML = classes[maxPos];
            flowerTypeAccuracyML = maxAccuracy;

            //Set text with accuracy results and flower type detected
            flowerSpinner.setSelection(flowerTypeAdapter.getPosition(flowerTypeML));
            accuracyPct.setText(String.format("%.2f%%", flowerTypeAccuracyML *100));
            accuracyText.setVisibility(View.VISIBLE);
            accuracyPct.setVisibility(View.VISIBLE);
            model.close();
        }
        catch (IOException e) {
            // TODO Handle exception
        }
    }
}

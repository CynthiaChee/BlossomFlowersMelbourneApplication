package com.example.mysearchingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysearchingapp.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {

    //Initializing variables
    ImageView accImageView;
    EditText accFullName, accUserName, accPassword, accConfirmPassword, accPhone;
    Button accCancelButton, accSaveButton;

    User user;
    byte[] userImageBytesArray;
    Bitmap userImageBitmap;
    String userFullName, username, password, passwordConfirm, phoneNumber;
    String loggedInUsername;

    // request variable
    final int GALLERY_REQUEST = 100;

    // database variables
    UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Find view by ID
        accImageView = findViewById(R.id.accPhoto);
        accFullName = findViewById(R.id.accNewFullName);
        accUserName = findViewById(R.id.accNewUserName);
        accPassword = findViewById(R.id.accNewPass);
        accConfirmPassword = findViewById(R.id.accConfirmPass);
        accPhone = findViewById(R.id.accNewPhone);
        accCancelButton = findViewById(R.id.accCancelButton);
        accSaveButton = findViewById(R.id.accSaveButton);

        SharedPreferences prefs = getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
        loggedInUsername = prefs.getString(Util.LOGGEDIN_USER, "");

        //Get user data
        user = userDatabaseHelper.getUser(loggedInUsername);

        userImageBytesArray = user.getImage();
        accFullName.setHint("Full name - " + user.getName());
        accUserName.setHint("Username - " + user.getUsername());
        accPhone.setHint("Phone No - " + user.getPhoneNo());

        try {
            accImageView.setImageBitmap(Util.getBitmapFromBytesArray(user.getImage()));

        } catch (Exception e) {
            Log.i("Error: ", e.toString());
        }
    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.flower_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When an option in the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.accountMenu:
                return true;
            case R.id.myordersMenu:
                Intent ordersIntent = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(ordersIntent);
                return true;
            case R.id.logoutMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Opening gallery
    public void accChangePicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //Results from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Uri imageUri = data.getData();  // image's address
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    userImageBitmap = BitmapFactory.decodeStream(inputStream);

                    userImageBytesArray = Util.getBytesArrayFromBitmap(userImageBitmap);

                    // assign bitmap to the image view
                    accImageView.setImageBitmap(userImageBitmap);
                } catch (FileNotFoundException e) {
                    Util.createToast(this, "File not found!");
                }
            }
        }
    }

    //If cancel changes is clicked
    public void cancelChangesClick(View view)
    {
        Intent homeIntent = new Intent(this, OrdersActivity.class);
        startActivity(homeIntent);
    }

    //If save changes is clicked
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void saveChangesClick(View view) {

        // get user input data
        userFullName = accFullName.getText().toString();
        username = accUserName.getText().toString();
        password = accPassword.getText().toString();
        passwordConfirm = accConfirmPassword.getText().toString();
        phoneNumber = accPhone.getText().toString();

        ContentValues contentValues = new ContentValues();

        //If new username already exists
        if (!username.equals("") && !username.equals(user.getUsername()) && userDatabaseHelper.userExists(username))
        {
            Util.createToast(this, "Username already in use.");
        }
        //If passwords do not match
        else if (!password.equals(passwordConfirm))
        {
            Util.createToast(this, "Passwords do not match.");
        }
        else
        {
            //If changes to profile picture made
            if (!userImageBytesArray.equals(user.getImage()))
            {
                contentValues.put(Util.PROFILE_PICTURE, userImageBytesArray);
                user.setImage(userImageBytesArray);
            }
            //If changes to full name made
            if (!userFullName.equals("") && !userFullName.equals(user.getName()))
            {
                contentValues.put(Util.NAME, userFullName);
                user.setName(userFullName);
            }
            //If changes to username made
            if (!username.equals("") && !username.equals(user.getUsername()))
            {
                contentValues.put(Util.USERNAME, username);
                user.setUsername(username);
            }
            //If changes to password made
            if (!password.equals("") && !password.equals(user.getPassword()))
            {
                contentValues.put(Util.PASSWORD, password);
                user.setPassword(password);
            }
            //If changes to phone number made
            if (!phoneNumber.equals("") && !phoneNumber.equals(user.getPhoneNo()))
            {
                contentValues.put(Util.PHONE_NO, phoneNumber);
                user.setPhoneNo(phoneNumber);
            }
            //If changes made, update data in database
            if (!contentValues.isEmpty())
            {
                int res = userDatabaseHelper.updateDetails(loggedInUsername, contentValues);
                if (res > 0) {
                    if (!loggedInUsername.equals(user.getUsername()))
                    {
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put(Util.USERNAME, user.getUsername());
                        OrderDatabaseHelper order_db = new OrderDatabaseHelper(this);
                        order_db.updateUsername(loggedInUsername, contentValues1);

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Util.LOGGEDIN_USER, user.getUsername());
                        editor.apply();

                        loggedInUsername = sharedPreferences.getString(Util.LOGGEDIN_USER, "");
                    }

                    Util.createToast(this, "Changes saved successfully");
                }
                else {
                    Util.createToast(this, "Changes could not be saved");
                }
            }
            // if user did not make any changes
            else
            {
                Util.createToast(this, "No changes made");
            }
            // go back to home page
            Intent backIntent = new Intent(this, OrdersActivity.class);
            startActivity(backIntent);
        }
    }
}

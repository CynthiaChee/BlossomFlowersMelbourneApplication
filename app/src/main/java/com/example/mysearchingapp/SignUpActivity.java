package com.example.mysearchingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysearchingapp.util.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class SignUpActivity extends AppCompatActivity {

    //Initializing variables
    UserDatabaseHelper myUserDBHelper;
    ImageView signupImage;
    EditText signupFullName, signupDOB, signupAddress, signupUserName, signupPassword, signupConfirmPassword, signupPhoneNo;
    Button createAccountButton;
    private Bitmap userImage;
    byte[] userImageBytes;
    public static final int PROFILE_PICTURE_REQUEST = 100;
    public static final int AUTOFILL_REQUEST = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        myUserDBHelper = new UserDatabaseHelper(this);

        //Find view by ID
        signupImage = findViewById(R.id.addPhoto);
        signupFullName = findViewById(R.id.fullNameEdit);
        signupDOB = findViewById(R.id.signupDateofBirthEditText);
        signupAddress = findViewById(R.id.signupAddressEditText);
        signupUserName = findViewById(R.id.usernameEdit);
        signupPassword = findViewById(R.id.passwordEditText);
        signupConfirmPassword = findViewById(R.id.confirmPasswordEdit);
        signupPhoneNo = findViewById(R.id.phoneNumberEdit);
        createAccountButton = findViewById(R.id.createAccountButton);
    }

    //Opens gallery
    public void addDisplayPicture(View view) {
        Intent profilePictureIntent = new Intent(Intent.ACTION_PICK);
        profilePictureIntent.setType("image/*");
        startActivityForResult(profilePictureIntent, PROFILE_PICTURE_REQUEST);
    }

    //Get results from intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_PICTURE_REQUEST){

            if (resultCode == RESULT_OK)
            {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    userImage = BitmapFactory.decodeStream(inputStream);

                    userImageBytes = Util.getBytesArrayFromBitmap(userImage);

                    signupImage.setImageBitmap(userImage);
                }
                catch (FileNotFoundException e) {
                    Util.createToast(this, "File not found!");
                }
            }
            else
            {
                setDefaultImage();
            }
        }
    }

    //Set default image if none chosen
    public void setDefaultImage()
    {
        Bitmap defaultPic = BitmapFactory.decodeResource(getResources(), R.drawable.person_icon);
        userImageBytes = Util.getBytesArrayFromBitmap(defaultPic);
        signupImage.setImageBitmap(defaultPic);
    }

    //If create account button is clicked
    public void createAccountClick(View view)
    {
        String fullName = signupFullName.getText().toString();
        String userName = signupUserName.getText().toString();
        String password = signupPassword.getText().toString();
        String confirmPassword = signupConfirmPassword.getText().toString();
        String phoneNo = signupPhoneNo.getText().toString();

        //If username already used
        if (myUserDBHelper.userExists(userName))
        {
            Util.createToast(SignUpActivity.this, "Username unavailable.");
        }
        else
        {
            //If passwords match
            if (password.equals(confirmPassword)) {

                //Set default image if none selected
                if (userImageBytes == null)
                {
                    setDefaultImage();
                }

                //Add new entry into user database
                long rowID = myUserDBHelper.insertUser(new User(userImageBytes, fullName, userName, password, phoneNo));

                //If entry successfully added
                if (rowID > 0) {
                    Util.createToast(SignUpActivity.this, "Registered successfully!");

                    //Return to login page
                    Intent goBackIntent = new Intent(this, MainActivity.class);
                    startActivity(goBackIntent);
                    finish();
                }

                //If entry creation unsuccessful
                else {
                    Util.createToast(SignUpActivity.this, "Error: unable to register user.");
                }
            }
            //If passwords do not match
            else {
                Util.createToast(SignUpActivity.this, "Passwords do not match.");
            }
        }
    }
}

package com.example.mysearchingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Initializing variables
    UserDatabaseHelper userDBHelper;
    EditText loginUserName, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDBHelper = new UserDatabaseHelper(this);

        //Find view by ID
        loginUserName = (EditText) findViewById(R.id.userNameEdit);
        loginPassword = (EditText) findViewById(R.id.passwordEditText);

    }

    //If login button is clicked
    public void loginClick(View view)
    {
        String username = loginUserName.getText().toString();
        String password = loginPassword.getText().toString();
        boolean result = userDBHelper.fetchUser(username, password);

        if (result){
            Intent orderIntent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(orderIntent);
            finish();
        }
        else{
            Toast.makeText(MainActivity.this, "User does not exist.", Toast.LENGTH_SHORT).show();
        }
    }

    //If sign up button is clicked
    public void signupClick(View view)
    {
        Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    //If login with fingerprint is clicked
    public void loginWithFingerprintClick(View view) {
        Intent biometricIntent = new Intent(MainActivity.this, BiometricActivity.class);
        startActivity(biometricIntent);
    }
    }

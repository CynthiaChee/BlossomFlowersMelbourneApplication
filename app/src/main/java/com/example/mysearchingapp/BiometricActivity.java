package com.example.mysearchingapp;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        //Prompt for login
        TextView messageTextView = findViewById(R.id.bioMessage);
        final Button biometricLoginButton = findViewById(R.id.biometricLoginButton);

        //Create a biometric manager object
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        //Check if user has access to biometric sensor
        switch (biometricManager.canAuthenticate())
        {
            //If user has access to biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                messageTextView.setText("Login with Fingerprint");
                messageTextView.setTextColor(Color.parseColor("#fafafa"));
                break;

            //If device has no biometric sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                messageTextView.setText("Fingerprint sensor not found on this device.");
                biometricLoginButton.setVisibility(View.GONE);
                break;

            //If biometric sensor is unavailable
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                messageTextView.setText("The biometric sensor is unavailable.");
                biometricLoginButton.setVisibility(View.GONE);
                break;

            //If no fingerprint record is stored in this device
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                messageTextView.setText("There is no fingerprint enrolled in this device.");
                biometricLoginButton.setVisibility(View.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(BiometricActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }
            //If authentication successful
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                biometricLoginButton.setText("Login Successful");

                // start home activity
                Intent homeIntent = new Intent(BiometricActivity.this, OrdersActivity.class);
                startActivity(homeIntent);
            }
            //If authentication unsuccessful
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //Prompt message
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Blossom Flowers Melbourne")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        biometricLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}

package com.example.javath;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load the fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Reference to the TextView for the company name animation
        TextView companyNameTextView = findViewById(R.id.companyNameTextView);
        companyNameTextView.startAnimation(fadeIn);

        // Delay to hold the splash screen for a few seconds before transitioning
        new Handler().postDelayed(() -> {
            // Start MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // Apply the fade-in transition for the MainActivity launch
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);

            // Close SplashActivity so it doesn’t remain in the back stack
            finish();
        }, 2000); // 3 seconds delay for splash screen
    }
}

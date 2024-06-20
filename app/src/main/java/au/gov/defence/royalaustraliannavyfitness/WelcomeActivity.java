package au.gov.defence.royalaustraliannavyfitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import au.gov.defence.royalaustraliannavyfitness.R;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Corresponding layout file


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ImageView logoGifImageView = findViewById(R.id.image); // Assuming you have an ImageView for the GIF


        Glide.with(this).load(R.raw.welcome1).into(logoGifImageView); // Loading GIF using Glide

        // Subscribe to topic
        // Handle failure
        FirebaseMessaging.getInstance().subscribeToTopic("ran")
                .addOnCompleteListener(Task::isSuccessful);


        // Delayed execution
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Services.getUserData(WelcomeActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid(),false);
            } else {

                SharedPreferences sharedPreferences = getSharedPreferences("RAN", MODE_PRIVATE);

                if (sharedPreferences.getBoolean("disclaimer", false)) {
                    Intent intent = new Intent(WelcomeActivity.this, EntryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(WelcomeActivity.this, DisclaimerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        }, 2000); // 2 seconds delay
    }
}

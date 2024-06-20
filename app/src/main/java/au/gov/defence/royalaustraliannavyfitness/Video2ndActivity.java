package au.gov.defence.royalaustraliannavyfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class Video2ndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video2nd);
        ImageView logoGifImageView = findViewById(R.id.image); // Assuming you have an ImageView for the GIF
        Glide.with(this).load(R.raw.welcome2).into(logoGifImageView); // Loading GIF using Glide

        AccountType accountType = (AccountType) getIntent().getSerializableExtra("accountType");
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra("accountType", accountType);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }, 2000); // 2 seconds delay
    }
}

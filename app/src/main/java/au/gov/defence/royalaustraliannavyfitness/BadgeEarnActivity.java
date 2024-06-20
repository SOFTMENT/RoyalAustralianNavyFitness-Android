package au.gov.defence.royalaustraliannavyfitness;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class BadgeEarnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_earn);

        ImageView imageView = findViewById(R.id.image); // Replace with your actual ImageView ID

        // Assuming badge is passed as an intent extra
        String badge = getIntent().getStringExtra("badge");

        int gifResource = R.drawable.bronze; // Default to bronze
        if ("BRONZE".equals(badge)) {
            gifResource = R.drawable.bronze;
        } else if ("SILVER".equals(badge)) {
            gifResource = R.drawable.silver;
        } else if ("GOLD".equals(badge)) {
            gifResource = R.drawable.gold;
        } else if ("PLATINUM".equals(badge)) {
            gifResource = R.drawable.platinum;
        }

        Glide.with(this).asGif().load(gifResource).into(imageView);

        // Dismiss the activity after 8 seconds
        new Handler().postDelayed(this::finish, 8000);
    }


}

package au.gov.defence.royalaustraliannavyfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import au.gov.defence.royalaustraliannavyfitness.Model.UserModel;
import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;

public class DisclaimerActivity extends AppCompatActivity {

    private ImageView checkBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        checkBtn = findViewById(R.id.done);


        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClicked();
            }
        });
    }

    private void checkClicked() {
        SharedPreferences sharedPreferences = getSharedPreferences("RAN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("disclaimer", true);
        editor.apply();
        Intent intent = new Intent(this, EntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}


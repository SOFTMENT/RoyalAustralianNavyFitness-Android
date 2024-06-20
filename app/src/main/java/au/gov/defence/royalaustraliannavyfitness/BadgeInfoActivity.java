package au.gov.defence.royalaustraliannavyfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class BadgeInfoActivity extends AppCompatActivity {
    private TextView completeSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_info);
        completeSession = findViewById(R.id.completeSession);
        fetchDataFromFirebase();
    }


    private void fetchDataFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .collection("Completed");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                completeSession.setText("You've completed " + count + " sessions");
            }
        });
    }
}

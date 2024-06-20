package au.gov.defence.royalaustraliannavyfitness;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameTF, emailTF, passwordTF;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Replace with your layout file name

        nameTF = findViewById(R.id.name); // Replace with your actual ID
        emailTF = findViewById(R.id.email); // Replace with your actual ID
        passwordTF = findViewById(R.id.password); // Replace with your actual ID
        Button signUpBtn = findViewById(R.id.signUp); // Replace with your actual ID
        // Assuming backView is used as a back button
        ImageView backView = findViewById(R.id.back); // Replace with your actual ID

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(v -> signupClicked());
        backView.setOnClickListener(v -> finish()); // To go back

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void signupClicked() {
        String sName = nameTF.getText().toString();
        String sEmail = emailTF.getText().toString();
        String sPassword = passwordTF.getText().toString();

        if (sName.isEmpty()) {
            Services.showCenterToast(SignUpActivity.this,"Enter Name");
            return;
        }

        if (sEmail.isEmpty()) {

            Services.showCenterToast(SignUpActivity.this,"Enter Email");
            return;
        }

        if (sPassword.isEmpty()) {
            Services.showCenterToast(SignUpActivity.this,"Enter Password");

            return;
        }

        ProgressHud.show(this,"Creating Account...");

        // Implement your progress HUD logic here
        auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        addUserOnFirebase(uid, sName, sEmail);
                    } else {
                        ProgressHud.dialog.dismiss();
                        Services.showDialog(SignUpActivity.this,"ERROR",task.getException().getMessage());

                    }
                });
    }

    private void addUserOnFirebase(String uid, String name, String email) {
        Map<String, Object> userModel = new HashMap<>();
        userModel.put("uid", uid);
        userModel.put("fullName", name);
        userModel.put("email", email);
        userModel.put("date", new Date()); // Ensure the date format is compatible with your database

        firestore.collection("Users").document(uid)
                .set(userModel)
                .addOnCompleteListener(task -> {
                    ProgressHud.dialog.dismiss();
                    if (task.isSuccessful()) {
                        Services.getUserData(SignUpActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                    } else {
                        Services.showDialog(SignUpActivity.this,"ERROR",task.getException().getMessage());

                    }
                });
    }


}

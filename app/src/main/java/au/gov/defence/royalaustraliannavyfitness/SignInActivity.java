package au.gov.defence.royalaustraliannavyfitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class SignInActivity extends AppCompatActivity {

    private EditText emailTF;
    private EditText passwordTF;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // Replace with your layout file name

        emailTF = findViewById(R.id.email); // Replace with your actual ID
        passwordTF = findViewById(R.id.password); // Replace with your actual ID
        TextView forgotPassword = findViewById(R.id.forgotPassword); // Replace with your actual ID
        Button loginBtn = findViewById(R.id.login); // Replace with your actual ID
        TextView createNewAccountBtn = findViewById(R.id.createNewAccount); // Replace with your actual ID

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> loginBtnClicked());
        forgotPassword.setOnClickListener(v -> forgotPasswordClicked());
        createNewAccountBtn.setOnClickListener(v -> createNewAccount());
    }

    private void createNewAccount() {
        // Start SignUp Activity
        startActivity(new Intent(this, SignUpActivity.class)); // Replace SignUpActivity with your actual activity
    }

    private void forgotPasswordClicked() {
        String sEmail = emailTF.getText().toString();
        if (sEmail.isEmpty()) {

            Services.showCenterToast(this,"Enter Email Address");
        }
        else {
            ProgressHud.show(this,"");
            auth.sendPasswordResetEmail(sEmail)
                    .addOnCompleteListener(task -> {
                     ProgressHud.dialog.dismiss();
                        if (task.isSuccessful()) {

                            Services.showDialog(SignInActivity.this,"Password Reset","Password reset link sent to your email.");
                        } else {
                            Services.showDialog(SignInActivity.this,"ERROR",task.getException().getMessage());
                        }
                    });
        }
    }

    private void loginBtnClicked() {
        String sEmail = emailTF.getText().toString();
        String sPassword = passwordTF.getText().toString();

        if (sEmail.isEmpty()) {

            Services.showCenterToast(this,"Enter Email Address");
            return;
        }

        if (sPassword.isEmpty()) {
            Services.showCenterToast(this,"Enter Password");
            return;
        }

        ProgressHud.show(this,"Login...");
        auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, task -> {
                    ProgressHud.dialog.dismiss();
                    if (task.isSuccessful()) {


                        Services.getUserData(SignInActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);

                    } else {
                        // If sign in fails, display a message to the user.
                        Services.showDialog(SignInActivity.this,"ERROR",task.getException().getMessage());
                    }
                });
    }


}


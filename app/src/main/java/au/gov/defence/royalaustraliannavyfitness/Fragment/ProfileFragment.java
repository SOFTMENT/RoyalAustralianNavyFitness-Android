package au.gov.defence.royalaustraliannavyfitness.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import au.gov.defence.royalaustraliannavyfitness.BadgeInfoActivity;
import au.gov.defence.royalaustraliannavyfitness.Model.UserModel;
import au.gov.defence.royalaustraliannavyfitness.R;
import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class ProfileFragment extends Fragment {

    private AppCompatButton logoutBtn, infoBtn;
    private LinearLayout deleteAccountBtn;
    private TextView fullName, email, noBadgesYetLabel;
    private LinearLayout bronze, silver, gold, platinum;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutBtn = view.findViewById(R.id.logout);
        deleteAccountBtn = view.findViewById(R.id.deleteAccount);
        infoBtn = view.findViewById(R.id.info);
        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        noBadgesYetLabel = view.findViewById(R.id.noBadgesEarnedYet);

        fullName.setText(UserModel.data.getFullName());
        email.setText(UserModel.data.getEmail());

        logoutBtn.setOnClickListener(v -> logout());
        deleteAccountBtn.setOnClickListener(v -> deleteAccount());
        infoBtn.setOnClickListener(v -> showBadgeInfo());

        bronze = view.findViewById(R.id.bronze);
        silver = view.findViewById(R.id.silver);
        gold = view.findViewById(R.id.gold);
        platinum = view.findViewById(R.id.platinum);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .collection("Completed");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();

                // Update UI based on count
                noBadgesYetLabel.setVisibility(count > 2 ? View.GONE : View.VISIBLE);
                bronze.setVisibility(count > 2 ? View.VISIBLE : View.GONE);
                silver.setVisibility(count > 8 ? View.VISIBLE : View.GONE);
                gold.setVisibility(count > 17 ? View.VISIBLE : View.GONE);
                platinum.setVisibility(count > 35 ? View.VISIBLE : View.GONE);
            }
        });

        return view;
    }

    private void logout() {
        Services.logout(getContext());
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("DELETE ACCOUNT");
        builder.setMessage("Are you sure you want to delete your account?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (auth.getCurrentUser() != null) {
                    String userId = auth.getCurrentUser().getUid();
                    ProgressHud.show(getContext(), "Deleting Account...");

                    auth.getCurrentUser().delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseFirestore.getInstance().collection("Users").document(userId).delete().addOnCompleteListener(task1 -> {
                                ProgressHud.dialog.dismiss();
                                if (task1.isSuccessful()) {
                                    logout();
                                } else {
                                    Services.showDialog(getContext(), "ERROR", task1.getException().getLocalizedMessage());
                                }
                            });
                        } else {
                            ProgressHud.dialog.dismiss();
                            showReLoginRequiredDialog();
                        }

                    });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showReLoginRequiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Re-Login Required");
        builder.setMessage("Delete account requires re-verification. Please login and try again.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showBadgeInfo() {
        startActivity(new Intent(getContext(), BadgeInfoActivity.class));
    }

    // Other methods and Firebase logic
}

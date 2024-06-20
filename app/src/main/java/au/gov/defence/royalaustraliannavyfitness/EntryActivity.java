package au.gov.defence.royalaustraliannavyfitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class EntryActivity extends AppCompatActivity {

    private AppCompatButton newJoinerBtn;
    private AppCompatButton navyMemberBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        newJoinerBtn = findViewById(R.id.preJoiner);
        navyMemberBtn = findViewById(R.id.navyMember);

        newJoinerBtn.setOnClickListener(v -> newJoinerClicked());
        navyMemberBtn.setOnClickListener(v -> navyMemberClicked());


    }


    private void newJoinerClicked() {
        Intent intent = new Intent(this, Video2ndActivity.class);
        intent.putExtra("accountType", AccountType.PREJOINER);
        startActivity(intent);
    }

    private void navyMemberClicked() {
        Intent intent = new Intent(this, Video2ndActivity.class);
        intent.putExtra("accountType", AccountType.PREJOINER);
        startActivity(intent);
    }


}

enum AccountType {
    NAVY,
    PREJOINER
}

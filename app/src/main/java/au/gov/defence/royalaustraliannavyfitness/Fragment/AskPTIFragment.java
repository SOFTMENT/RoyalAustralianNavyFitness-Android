package au.gov.defence.royalaustraliannavyfitness.Fragment;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import au.gov.defence.royalaustraliannavyfitness.R;

public class AskPTIFragment extends Fragment {

    private EditText mEditText;
    private Button sendBtn;
    private final String placeholderText = "Type your question here...";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_p_t_i, container, false);

        mEditText = view.findViewById(R.id.question);
        sendBtn = view.findViewById(R.id.send);

        mEditText.setText(placeholderText);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && mEditText.getText().toString().equals(placeholderText)) {
                    mEditText.setText("");
                    mEditText.setTextColor(getResources().getColor(android.R.color.black));
                } else if (!hasFocus && mEditText.getText().toString().isEmpty()) {
                    mEditText.setText(placeholderText);
                    mEditText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = mEditText.getText().toString();
                if (question.isEmpty() || question.equals(placeholderText)) {
                    showToast("Enter Question");
                } else {
                    sendEmail(question);
                }
            }
        });

        return view;
    }

    private void sendEmail(String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Use 'mailto:' to ensure only email apps respond
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"pcp.corro@outlook.com"}); // Recipient's email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ask a PTI"); // Email subject
        emailIntent.putExtra(Intent.EXTRA_TEXT, body); // Email body

        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "No email client installed.", Toast.LENGTH_SHORT).show();
        }

    }


    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

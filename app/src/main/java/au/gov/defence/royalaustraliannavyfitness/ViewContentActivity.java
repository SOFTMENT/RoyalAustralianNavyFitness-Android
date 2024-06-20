package au.gov.defence.royalaustraliannavyfitness;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import au.gov.defence.royalaustraliannavyfitness.Model.CategoryModel;
import au.gov.defence.royalaustraliannavyfitness.Model.ContentModel;
import au.gov.defence.royalaustraliannavyfitness.Model.MultiVideoModel;
import au.gov.defence.royalaustraliannavyfitness.Model.UserModel;
import au.gov.defence.royalaustraliannavyfitness.Util.Constants;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class ViewContentActivity extends AppCompatActivity  {

    private VideoView videoView;
    private ProgressBar progressBar;
    private WebView webView;
    private TextView topTitle, hyperLink;
    private AppCompatButton completeBtn;
    private LinearLayout nextBtn;


    private List<ContentModel> contentModels;
    private int position = 0;
    private int count = 0;
    private TextView nextBtnCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        // Initialize your views
        videoView = findViewById(R.id.videoView);
        webView = findViewById(R.id.webView);
        topTitle = findViewById(R.id.subCategoryName);
        hyperLink = findViewById(R.id.catName);
        completeBtn = findViewById(R.id.complete);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showConfirmationDialog();
            }
        });

        progressBar = findViewById(R.id.progressbar);
        nextBtnCount = findViewById(R.id.count);
        contentModels = (List<ContentModel>) getIntent().getSerializableExtra("contentModels");
        String type = getIntent().getStringExtra("type");
        position = getIntent().getIntExtra("position",0);



        if (Objects.equals(type, "Informations")) {
            completeBtn.setVisibility(View.GONE);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();
                int currentPosition = videoView.getCurrentPosition();

                Intent fullscreenIntent = new Intent(ViewContentActivity.this, FullscreenVideoActivity.class);
                fullscreenIntent.setData(Uri.parse(Constants.AWS_BASE_URL+"/"+contentModels.get(position).multiVideoModels.get(0).getVideoURL()));
                fullscreenIntent.putExtra("currentPosition", currentPosition);
                startActivity(fullscreenIntent);
            }
        });

        ContentModel contentModel = contentModels.get(position);
        nextBtn = findViewById(R.id.nextBtn);
        updateUI(contentModel);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (contentModels.size() > (position + 1)) {
                        ContentModel currentModel = contentModels.get(position);

                        if (currentModel.getMultiVideoModels().size() == (count + 1)) {
                            count = 0;
                            position++;
                            Services.showCenterToast(ViewContentActivity.this,"Session : " + (position + 1));
                            updateUI(contentModels.get(position));
                        } else if (currentModel.getMultiVideoModels().size() > (count + 1)) {
                            count++;
                            if (count == (currentModel.getMultiVideoModels().size() - 1)) {
                                if (contentModels.size() > (position + 1)) {
                                    nextBtnCount.setText("Session " + (position + 2));
                                } else {
                                    nextBtn.setVisibility(View.GONE);
                                }
                            } else {
                               nextBtnCount.setText("Video " + (count + 2));
                            }

                            Services.showCenterToast(ViewContentActivity.this,"Video : " + (count + 1));
                            playVideo(currentModel.getMultiVideoModels().get(count));
                        } else {
                            nextBtn.setVisibility(View.GONE);
                        }
                    } else {
                        nextBtn.setVisibility(View.GONE);
                    }
                }
            });




    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to mark this session as completed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeSession();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void completeSession() {
        completeBtn.setText("Completed");
        completeBtn.setTextColor(Color.WHITE);
        completeBtn.setBackgroundDrawable(AppCompatResources.getDrawable(ViewContentActivity.this,R.drawable.radius_8_green));
       Services.showCenterToast(this,"Completed");
        completeBtn.setClickable(false);
        completeBtn.setEnabled(false);

        Map map = new HashMap();
        map.put("id",contentModels.get(position).getId());

        // Assuming 'userUid' and 'contentModelId' are obtained and valid
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Completed").document(contentModels.get(position).getId())
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String userUid = UserModel.data.getUid(); // Replace with your method of getting the user's UID

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Query query = db.collection("Users").document(userUid).collection("Completed");

                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int count = task.getResult().size();
                                        String badgeType = null;

                                        if (count == 3) {
                                            badgeType = "BRONZE";
                                        } else if (count == 9) {
                                            badgeType = "SILVER";
                                        } else if (count == 18) {
                                            badgeType = "GOLD";
                                        } else if (count == 36) {
                                            badgeType = "PLATINUM";
                                        }

                                        if (badgeType != null) {
                                            Intent intent = new Intent(ViewContentActivity.this, BadgeEarnActivity.class);
                                            intent.putExtra("badge", badgeType);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                        }

                    }
                });
    }

    private void updateUI(ContentModel contentModel) {
        // Logic for next button visibility and text
        if (contentModel.getMultiVideoModels().isEmpty()) {
            nextBtn.setVisibility(View.GONE);
        } else if (contentModel.getMultiVideoModels().size() == 1) {
            if (contentModels.size() == (position + 1)) {
                nextBtn.setVisibility(View.GONE);
            } else {
                nextBtnCount.setText("Session " + (position + 2));
            }
        } else {
            nextBtnCount.setText("Video 2");
        }

        if (contentModel.getTitle() != null) {
            topTitle.setText(contentModel.getTitle());
        }

        FirebaseFirestore.getInstance().collection("Users").document(UserModel.data.getUid()).collection("Completed").document(contentModel.getId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        completeBtn.setText("Completed");
                        completeBtn.setTextColor(Color.WHITE);

                        completeBtn.setBackgroundDrawable(AppCompatResources.getDrawable(ViewContentActivity.this,R.drawable.radius_8_green));


                        completeBtn.setEnabled(false);
                    }
                });

        // Load PDF link in WebView if available
        if (contentModel.getPdfLink() != null) {
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            WebSettings webSettings = webView.getSettings();
            webSettings.setDomStorageEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.setScrollbarFadingEnabled(false);
            webView.setScrollbarFadingEnabled(false);

            progressBar = findViewById(R.id.progressbar);
            progressBar.setMax(100);
            progressBar.setProgress(0);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView webView, int progress) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                    setTitle("Loading...");
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                        setTitle(webView.getTitle());
                    }
                    super.onProgressChanged(webView, progress);
                }
            });

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (Objects.equals(view.getTitle(), ""))
                        view.reload();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);

                    progressBar.setVisibility(View.GONE);
                }
            });

            try {

                String url= URLEncoder.encode(contentModel.getPdfLink(),"UTF-8");

                webView.postDelayed(() -> webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url), 100);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE); // Hide WebView if no PDF link
        }

        // Load video if available
        if (contentModel.multiVideoModels != null && !contentModel.multiVideoModels.isEmpty()) {

            MultiVideoModel multiVideoModel = contentModel.multiVideoModels.get(0); // Assuming first video
            videoView.post(new Runnable() {
                @Override
                public void run() {
                    // Now the VideoView has been laid out and should have width and height
                    adjustVideoViewAspectRatio(videoView, multiVideoModel.getRatio());
                }
            });
            if (multiVideoModel.getVideoURL() != null) {
                playVideo(multiVideoModel);
            }
        } else {
            videoView.setVisibility(View.GONE); // Hide video view if no video
        }



        // Set hyper link if available
        if (contentModel.getHyperLink() != null && !contentModel.getHyperLink().isEmpty()) {
            hyperLink.setText(contentModel.getHyperLink());
            hyperLink.setVisibility(View.VISIBLE);
        } else {
            hyperLink.setVisibility(View.GONE);
        }
        // Hyperlink handling
        if (contentModel.getHyperLink() != null && !contentModel.getHyperLink().isEmpty()) {
            hyperLink.setVisibility(View.VISIBLE);

            hyperLink.setText(contentModel.getHyperLink());
            hyperLink.setClickable(true);
            hyperLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contentModels != null && position < contentModels.size()) {
                        String hyperLinkId = contentModels.get(position).getHyperLinkId();
                        if (hyperLinkId != null) {
                            // Accessing Firestore for "Workouts" collection
                            FirebaseFirestore.getInstance().collection("Workouts").document(hyperLinkId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            CategoryModel catModel = documentSnapshot.toObject(CategoryModel.class);
                                            if (catModel != null) {
                                                // Dismiss the current activity and perform delegate action

                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("type", "Workouts");
                                                returnIntent.putExtra("title",catModel.getTitle());
                                                returnIntent.putExtra("id",catModel.getId());
                                                setResult(Activity.RESULT_OK, returnIntent);
                                                finish();
                                            }
                                        }
                                    });


                            FirebaseFirestore.getInstance().collection("Informations").document(hyperLinkId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            CategoryModel catModel = documentSnapshot.toObject(CategoryModel.class);
                                            if (catModel != null) {


                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("type", "Informations");
                                                returnIntent.putExtra("title",catModel.getTitle());
                                                returnIntent.putExtra("id",catModel.getId());
                                                setResult(Activity.RESULT_OK, returnIntent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                }
            });
        } else {
            hyperLink.setVisibility(View.GONE);

        }
    }


    private void adjustVideoViewAspectRatio(VideoView videoView, double aspectRatio) {
        int width = videoView.getWidth();
        // Assuming you are adjusting height based on the width and aspect ratio
        int newHeight = (int) (width / aspectRatio);
        // Update the layout parameters
        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.height = newHeight;

        videoView.setLayoutParams(params);
    }
    private void playVideo(MultiVideoModel multiVideoModel) {
        Uri videoUri = Uri.parse(Constants.AWS_BASE_URL + "/"+ multiVideoModel.getVideoURL());
        videoView.setVideoURI(videoUri);
        videoView.start();
        // You might want to use MediaController with VideoView for better user experience
    }



}

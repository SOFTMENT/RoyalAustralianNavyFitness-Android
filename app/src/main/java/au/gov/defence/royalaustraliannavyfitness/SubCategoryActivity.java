package au.gov.defence.royalaustraliannavyfitness;

import static au.gov.defence.royalaustraliannavyfitness.Util.Services.getAllSubCategories;
import static au.gov.defence.royalaustraliannavyfitness.Util.Services.getAllVideos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import au.gov.defence.royalaustraliannavyfitness.Adapter.ContentAdapter;
import au.gov.defence.royalaustraliannavyfitness.Interface.ContentCallback;
import au.gov.defence.royalaustraliannavyfitness.Interface.MultiVideoCallback;
import au.gov.defence.royalaustraliannavyfitness.Model.ContentModel;
import au.gov.defence.royalaustraliannavyfitness.Model.MultiVideoModel;
import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class SubCategoryActivity extends AppCompatActivity {

    private String type;
    private String catName;
    private String catId;
    private List<ContentModel> contentModels;
    private TextView catLabel;
    private TextView noSubcategoryAvailable;
    private RecyclerView recyclerView;
    private ImageView backView;
    private ContentAdapter adapter;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        // Assuming you are passing these via intent
        type = getIntent().getStringExtra("TYPE");
        catName = getIntent().getStringExtra("CAT_NAME");
        catId = getIntent().getStringExtra("CAT_ID");

        catLabel = findViewById(R.id.categoryName);
        noSubcategoryAvailable = findViewById(R.id.noSubCategoriesAvailable);
        recyclerView = findViewById(R.id.recyclerView);
        backView = findViewById(R.id.back);

        loadApp();

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // Handle the Intent...
                            if (data != null) {
                                String type = data.getStringExtra("type");
                                String title = data.getStringExtra("title");
                                String id = data.getStringExtra("id");

                                SubCategoryActivity.this.type = type;
                                SubCategoryActivity.this.catName = title;
                                SubCategoryActivity.this.catId = id;
                                loadApp();
                            }
                        }
                    }
                });


    }

    private void loadApp() {
        if (type == null || catName == null || catId == null) {
            finish(); // Close the activity
            return;
        }

        catLabel.setText(catName);

        backView.setOnClickListener(v -> finish());

        contentModels = new ArrayList<>();
        setupRecyclerView();

        ProgressHud.show(this,"");

        getAllSubCategories(type, catId, new ContentCallback() {
            @Override
            public void onCallback(List<ContentModel> contentList) {
                ProgressHud.dialog.dismiss();
                contentModels.clear();
                if (contentList != null) {
                    contentModels.addAll(contentList);
                }
                if (contentModels.size() > 0) {
                    noSubcategoryAvailable.setVisibility(View.GONE);
                }
                else {
                    noSubcategoryAvailable.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContentAdapter(this,contentModels, this::onContentClicked, this::onContent);
        recyclerView.setAdapter(adapter);
    }

    private void onContent(ContentModel content,int position) {
        getAllVideos(type, catId, content.id, new MultiVideoCallback() {
            @Override
            public void onCallback(List<MultiVideoModel> videoList) {
                contentModels.get(position).multiVideoModels.clear();
                if (videoList != null) {
                    contentModels.get(position).multiVideoModels.addAll(videoList);
                }
            }
        });
    }
    private void onContentClicked(ContentModel content,int position) {
        Intent intent = new Intent(this, ViewContentActivity.class);
        intent.putExtra("contentModels", (Serializable) contentModels);
        intent.putExtra("type",type);
        intent.putExtra("position", position);
        someActivityResultLauncher.launch(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && resultCode == RESULT_OK) {
            // Handle the data returned from Activity B
        }
    }
}

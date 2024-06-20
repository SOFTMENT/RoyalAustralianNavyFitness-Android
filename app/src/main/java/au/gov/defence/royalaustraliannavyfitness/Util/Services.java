package au.gov.defence.royalaustraliannavyfitness.Util;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import au.gov.defence.royalaustraliannavyfitness.EntryActivity;
import au.gov.defence.royalaustraliannavyfitness.Interface.CategoryCallback;
import au.gov.defence.royalaustraliannavyfitness.Interface.ContentCallback;
import au.gov.defence.royalaustraliannavyfitness.Interface.MultiVideoCallback;
import au.gov.defence.royalaustraliannavyfitness.Model.CategoryModel;
import au.gov.defence.royalaustraliannavyfitness.Model.ContentModel;
import au.gov.defence.royalaustraliannavyfitness.Model.MultiVideoModel;
import au.gov.defence.royalaustraliannavyfitness.Model.UserModel;
import au.gov.defence.royalaustraliannavyfitness.R;
import au.gov.defence.royalaustraliannavyfitness.SignInActivity;
import au.gov.defence.royalaustraliannavyfitness.TabbarActivity;

public class Services {
    public static void showCenterToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    public static void showDialog(Context context,String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.error_message_layout, null);
        TextView titleView = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        titleView.setText(title);
        msg.setText(message);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });

        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();

        }

    }
    public static void logout(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, EntryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
    public static void getUserData(Context activity, String uid, boolean showProgress) {

        if (showProgress) {
            ProgressHud.show(activity,"");
        }

        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (showProgress) {
                    ProgressHud.dialog.dismiss();
                }

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            UserModel.data = documentSnapshot.toObject(UserModel.class);
                            Intent intent = new Intent(activity, TabbarActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);

                        }
                        else {
                            Intent intent = new Intent(activity, EntryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                    }
                    else {
                        Services.showDialog(activity, "ERROR", "Something went wrong");

                    }
                }
                else {
                    Services.showDialog(activity, "ERROR", task.getException().getLocalizedMessage());

                }

            }
        });
    }
    public static void getAllSubCategories(String type, String catId, ContentCallback callback) {

        FirebaseFirestore.getInstance().collection(type).document(catId).collection("Sub")
                .orderBy("orderIndex").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        List<ContentModel> contents = new ArrayList<>();
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                ContentModel contentModel = document.toObject(ContentModel.class);
                                contents.add(contentModel);
                            }
                        }
                        callback.onCallback(contents);
                    }
                });
    }


    public static void getAllVideos(String type, String catId, String subCatId, MultiVideoCallback callback) {

        FirebaseFirestore.getInstance().collection(type)
                .document(catId)
                .collection("Sub")
                .document(subCatId)
                .collection("Videos")
                .orderBy("orderIndex")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<MultiVideoModel> videos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MultiVideoModel videoModel = document.toObject(MultiVideoModel.class);
                            videos.add(videoModel);
                        }
                        callback.onCallback(videos);
                    } else {
                        callback.onCallback(null);
                    }
                });
    }
    public static void getAllCategories(String type, CategoryCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(type)
                .orderBy("orderIndex")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<CategoryModel> categories = new ArrayList<>();
                            if (task.getResult() != null  && !task.getResult().isEmpty()) {
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    CategoryModel category = document.toObject(CategoryModel.class);
                                    categories.add(category);
                                }
                            }
                            callback.onCallback(categories);

                        }
                        else {
                            callback.onCallback(null);
                        }


                    }
                });
    }

}


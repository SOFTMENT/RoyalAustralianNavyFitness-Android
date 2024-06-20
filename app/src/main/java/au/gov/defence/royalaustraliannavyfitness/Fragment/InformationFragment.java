package au.gov.defence.royalaustraliannavyfitness.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import au.gov.defence.royalaustraliannavyfitness.Adapter.CategoryAdapter;
import au.gov.defence.royalaustraliannavyfitness.Interface.CategoryCallback;
import au.gov.defence.royalaustraliannavyfitness.Model.CategoryModel;
import au.gov.defence.royalaustraliannavyfitness.R;
import au.gov.defence.royalaustraliannavyfitness.SubCategoryActivity;
import au.gov.defence.royalaustraliannavyfitness.Util.ProgressHud;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class InformationFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CategoryModel> categoryModels;
    private TextView noCategoriesAvailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        categoryModels = new ArrayList<> ();

        noCategoriesAvailable = view.findViewById(R.id.noCategoriesAvailable);

        CategoryAdapter adapter = new CategoryAdapter(getContext(),categoryModels, this::onCategoryClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadCategories(adapter);

        return view;
    }

    private void loadCategories(CategoryAdapter adapter) {
        ProgressHud.show(getContext(),"");
        Services.getAllCategories("Informations", new CategoryCallback() {
            @Override
            public void onCallback(List<CategoryModel> categoryList) {

                ProgressHud.dialog.dismiss();
                categoryModels.clear();
                categoryModels.addAll(categoryList);

                if (categoryModels.size() > 0) {
                    noCategoriesAvailable.setVisibility(View.GONE);
                }
                else {
                    noCategoriesAvailable.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();

            }
        });

    }

    private void onCategoryClicked(CategoryModel category) {
        Intent intent = new Intent(getContext(), SubCategoryActivity.class);
        intent.putExtra("TYPE","Informations");
        intent.putExtra("CAT_NAME",category.title);
        intent.putExtra("CAT_ID",category.id);
        startActivity(intent);
    }
}

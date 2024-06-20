package au.gov.defence.royalaustraliannavyfitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import au.gov.defence.royalaustraliannavyfitness.Model.CategoryModel;
import au.gov.defence.royalaustraliannavyfitness.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryModel> categoryModels;
    private final OnCategoryClickListener listener;

    private Context context;
    public CategoryAdapter(Context context,List<CategoryModel> categoryModels, OnCategoryClickListener listener) {
        this.categoryModels = categoryModels;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel category = categoryModels.get(position);
        holder.bind(category,context, listener);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        private ImageView imageView;


        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.desc);
            imageView = view.findViewById(R.id.img);

        }

        void bind(final CategoryModel category, Context context, final OnCategoryClickListener listener) {
            title.setText(category.getTitle());
            description.setText(category.desc);
            Glide.with(context).load(category.getImage()).placeholder(R.drawable.placeholder).into(imageView);
            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);
    }
}

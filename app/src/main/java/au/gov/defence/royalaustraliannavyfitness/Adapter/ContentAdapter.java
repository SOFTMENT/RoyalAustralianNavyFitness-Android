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

import au.gov.defence.royalaustraliannavyfitness.Model.ContentModel;
import au.gov.defence.royalaustraliannavyfitness.R;
import au.gov.defence.royalaustraliannavyfitness.Util.Services;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private final List<ContentModel> contentModels;
    private final OnContentClickListener listener;
    private final  OnContentListener onContentListener;

    private final Context context;


    public ContentAdapter(Context context, List<ContentModel> contentModels, OnContentClickListener listener, OnContentListener onContentListener) {
        this.contentModels = contentModels;
        this.listener = listener;
        this.context = context;
        this.onContentListener = onContentListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_layout_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentModel contentModel = contentModels.get(position);
        holder.bind(context, contentModel, listener, onContentListener);
    }

    @Override
    public int getItemCount() {
        return contentModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Define your ViewHolder views here
        TextView title, videoCount;
        private ImageView imageView, pdf, video;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            videoCount = view.findViewById(R.id.videoCount);
            imageView = view.findViewById(R.id.img);
            pdf = view.findViewById(R.id.PDF);
            video = view.findViewById(R.id.VIDEO);

        }

        void bind(final Context context, final ContentModel contentModel, final OnContentClickListener listener, final  OnContentListener onContentListener) {
            // Bind data to views
            title.setText(contentModel.title);
            if (contentModel.videoCount > 1) {
                videoCount.setText(contentModel.videoCount + " videos");
            } else {
                videoCount.setText(contentModel.videoCount + " video");
            }
            Glide.with(context).load(contentModel.image).placeholder(R.drawable.placeholder).into(imageView);

            if (contentModel.pdfLink.isEmpty()) {
                pdf.setVisibility(View.GONE);
            } else {
                pdf.setVisibility(View.VISIBLE);
            }

            if (contentModel.videoCount > 0) {
                video.setVisibility(View.VISIBLE);
                videoCount.setVisibility(View.VISIBLE);
            } else {
                video.setVisibility(View.GONE);
                videoCount.setVisibility(View.GONE);
            }

            onContentListener.onContent(contentModel,getAdapterPosition());

            itemView.setOnClickListener(v -> listener.onContentClick(contentModel, getAdapterPosition()));
        }
    }

    public interface OnContentListener {
        void onContent(ContentModel contentModel, int position);
    }
    public interface OnContentClickListener {
        void onContentClick(ContentModel contentModel, int position);
    }
}




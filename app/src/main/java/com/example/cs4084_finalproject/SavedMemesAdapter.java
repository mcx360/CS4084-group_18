package com.example.cs4084_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class SavedMemesAdapter extends RecyclerView.Adapter<SavedMemesAdapter.ViewHolder> {

    private final ArrayList<String> memesList;
    private final Fragment fragment;

    public SavedMemesAdapter(ArrayList<String> memesList, Fragment fragment) {
        this.memesList = memesList;
        this.fragment = fragment;
    }

    @Override
    public SavedMemesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_meme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedMemesAdapter.ViewHolder holder, int position) {
        String memeUrl = memesList.get(position);
        Glide.with(fragment)
                .load(memeUrl)
                .centerCrop()
                .into(holder.memeImageView);

        holder.itemView.setOnClickListener(v -> {
            MemeDetailFragment detailFragment = MemeDetailFragment.newInstance(memesList.get(position));
            fragment.getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, detailFragment)
                    .addToBackStack(null) // So user can press back
                    .commitAllowingStateLoss();
        });

    }

    @Override
    public int getItemCount() {
        return memesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView memeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            memeImageView = itemView.findViewById(R.id.imageViewMeme);
        }
    }
}

package com.example.cs4084_finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MemeDetailFragment extends Fragment {

    private static final String ARG_MEME_URL = "meme_url";
    private String memeUrl;
    private ImageView featuredImage;

    public static MemeDetailFragment newInstance(String memeUrl) {
        MemeDetailFragment fragment = new MemeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEME_URL, memeUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meme_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        featuredImage = view.findViewById(R.id.featuredImage);
        ImageButton shareButton = view.findViewById(R.id.btn_share);
        ImageButton likeButton = view.findViewById(R.id.btn_like);

        if (getArguments() != null) {
            memeUrl = getArguments().getString(ARG_MEME_URL);
            Glide.with(this)
                    .load(memeUrl)
                    .into(featuredImage);
        }

        DBHandler dbHandler = new DBHandler(requireContext());
        ArrayList<String> savedMemes = dbHandler.readMemes();

        if (savedMemes.contains(memeUrl)) {
            likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
        } else {
            likeButton.setColorFilter(null);
        }

        likeButton.setOnClickListener(v -> {
            if (savedMemes.contains(memeUrl)) {
                dbHandler.deleteMeme(memeUrl);
                Toast.makeText(requireContext(), "Meme unsaved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(null);
                savedMemes.remove(memeUrl);
            } else {
                dbHandler.addNewMeme(memeUrl);
                Toast.makeText(requireContext(), "Meme saved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
                savedMemes.add(memeUrl);
            }
        });

        shareButton.setOnClickListener(v -> shareMeme());
    }

    private void shareMeme() {
        if (memeUrl == null) return;

        Glide.with(this)
                .asBitmap()
                .load(memeUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            File cachePath = new File(requireContext().getCacheDir(), "images");
                            cachePath.mkdirs();
                            File file = new File(cachePath, "shared_meme.png");
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            stream.close();

                            Uri contentUri = FileProvider.getUriForFile(requireContext(), "com.example.cs4084_finalproject.fileprovider", file);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "Share meme using"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error sharing meme", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}

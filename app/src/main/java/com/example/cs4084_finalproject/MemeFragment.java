package com.example.cs4084_finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.toolbox.ImageRequest;

public class MemeFragment extends Fragment {
    private static final String ARG_MEME_URL = "meme_url";
    private ImageView featuredImage;
    private TextView textView;

    public static MemeFragment newInstance(String memeUrl) {
        MemeFragment fragment = new MemeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEME_URL, memeUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meme, container, false);
        featuredImage = view.findViewById(R.id.featuredImage);
        textView = view.findViewById(R.id.textView3);

        if (getArguments() != null) {
            String memeUrl = getArguments().getString(ARG_MEME_URL);
            if (memeUrl != null && !memeUrl.equals("noMemeReturned")) {
                loadMemeImage(memeUrl);
                textView.setText("API Name");
            } else {
                featuredImage.setImageResource(R.drawable.meme);
                textView.setText("Error loading meme");
            }
        }

        return view;
    }

    private void loadMemeImage(String memeUrl) {
        ImageRequest imageRequest = new ImageRequest(
                memeUrl,
                response -> featuredImage.setImageBitmap(response),
                0, 0, ImageView.ScaleType.FIT_CENTER, null,
                error -> {
                    textView.setText("Failed to load image");
                }
        );
        mySingleton.getInstance(requireContext()).addToRequestQueue(imageRequest);
    }
}
package com.example.cs4084_finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.toolbox.ImageRequest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;


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
        ImageButton shareButton = view.findViewById(R.id.btn_share);
        shareButton.setOnClickListener(v -> shareMeme());

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

    public void shareMeme(){
        BitmapDrawable drawable = (BitmapDrawable) featuredImage.getDrawable();
        if (drawable == null) return;

        Bitmap bitmap = drawable.getBitmap();

        try {
            File cachePath = new File(requireContext().getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "meme.png");
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
        }
    }
}
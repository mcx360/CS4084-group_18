package com.example.cs4084_finalproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MemeDetailFragment extends Fragment {

    private static final String ARG_MEME_URL = "meme_url";

    private String memeUrl;

    public MemeDetailFragment() {
        // Required empty public constructor
    }

    public static MemeDetailFragment newInstance(String memeUrl) {
        MemeDetailFragment fragment = new MemeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEME_URL, memeUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memeUrl = getArguments().getString(ARG_MEME_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meme_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView featuredImage = view.findViewById(R.id.featuredImage);
        ImageButton likeButton = view.findViewById(R.id.btn_like);


        if (memeUrl != null) {
            Glide.with(this)
                    .load(memeUrl)
                    .fitCenter()
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
                savedMemes.remove(memeUrl); // update local list too
            } else {
                dbHandler.addNewMeme(memeUrl);
                Toast.makeText(requireContext(), "Meme saved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
                savedMemes.add(memeUrl); // update local list too
            }
        });
    }
}

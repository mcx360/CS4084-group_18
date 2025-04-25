package com.example.cs4084_finalproject;

import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class JokeFragment extends Fragment {

    private static final String ARG_JOKE = "joke";
    private TextView jokeText;

    public static JokeFragment newInstance(String joke) {
        JokeFragment fragment = new JokeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JOKE, joke);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, container, false);
        jokeText = view.findViewById(R.id.jokeText);
        ImageButton likeButton = view.findViewById(R.id.btn_like);

        if (getArguments() != null) {
            String joke = getArguments().getString(ARG_JOKE);
            jokeText.setText(joke);


            DBHandler dbHandler = new DBHandler(requireContext());
            ArrayList<String> savedJokes = dbHandler.readJokes();
            if (savedJokes.contains(joke)) {
                likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
            }
        }

        likeButton.setOnClickListener(v -> {
            String joke = jokeText.getText().toString().trim();
            DBHandler dbHandler = new DBHandler(requireContext());
            ArrayList<String> savedJokes = dbHandler.readJokes();

            if (savedJokes.contains(joke)) {
                dbHandler.deleteJoke(joke);
                Toast.makeText(requireContext(), "Joke unsaved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(null);
            } else {
                dbHandler.addNewJoke(joke);
                Toast.makeText(requireContext(), "Joke saved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
            }
        });


        ImageButton share_button = view.findViewById(R.id.btn_share);
        share_button.setOnClickListener(view1 -> shareJoke());

        return view;
    }

    public void shareJoke() {
         String joke = jokeText.getText().toString();
         if(joke.isEmpty()){
             return;
         }
         Intent shareJoke_intent = new Intent(Intent.ACTION_SEND);
         shareJoke_intent.setType("text/plain");
         shareJoke_intent.putExtra(Intent.EXTRA_TEXT,joke);
         startActivity(Intent.createChooser(shareJoke_intent, "Share this joke using"));

    }
}
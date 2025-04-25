package com.example.cs4084_finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JokeDetailFragment extends Fragment {

    private static final String ARG_JOKE_TEXT = "joke_text";

    private String jokeText;
    private DBHandler dbHandler;
    private ArrayList<String> savedJokes;

    public JokeDetailFragment() {
    }

    public static JokeDetailFragment newInstance(String jokeText) {
        JokeDetailFragment fragment = new JokeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JOKE_TEXT, jokeText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jokeText = getArguments().getString(ARG_JOKE_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_joke_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView jokeTextView = view.findViewById(R.id.jokeText);
        ImageButton likeButton = view.findViewById(R.id.btn_like);
        ImageButton shareButton = view.findViewById(R.id.btn_share);

        jokeTextView.setText(jokeText);

        dbHandler = new DBHandler(requireContext());
        savedJokes = dbHandler.readJokes();

        if (savedJokes.contains(jokeText)) {
            likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
        } else {
            likeButton.setColorFilter(null);
        }

        likeButton.setOnClickListener(v -> {
            if (savedJokes.contains(jokeText)) {
                dbHandler.deleteJoke(jokeText);
                Toast.makeText(requireContext(), "Joke unsaved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(null);
                savedJokes.remove(jokeText);
            } else {
                dbHandler.addNewJoke(jokeText);
                Toast.makeText(requireContext(), "Joke saved", Toast.LENGTH_SHORT).show();
                likeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white));
                savedJokes.add(jokeText);
            }
        });

        shareButton.setOnClickListener(v -> shareJoke());
    }

    private void shareJoke() {
        if (jokeText == null || jokeText.isEmpty()) return;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, jokeText);
        startActivity(Intent.createChooser(shareIntent, "Share this joke using"));
    }
}

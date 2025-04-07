package com.example.cs4084_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        if (getArguments() != null) {
            String joke = getArguments().getString(ARG_JOKE);
            jokeText.setText(joke);
        }

        ImageButton likeButton = view.findViewById(R.id.btn_like);
        likeButton.setOnClickListener(v -> {
            String joke = jokeText.getText().toString();
            DBHandler dbHandler = new DBHandler(requireContext());
            dbHandler.addNewJoke(joke);
            Toast.makeText(requireContext(), "Joke saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
package com.example.cs4084_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
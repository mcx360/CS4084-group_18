package com.example.cs4084_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        return view;
    }
}
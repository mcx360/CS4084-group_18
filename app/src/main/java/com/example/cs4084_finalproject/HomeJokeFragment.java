package com.example.cs4084_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeJokeFragment extends Fragment {

    private ViewPager2 viewPager;
    private List<String> jokes = new ArrayList<>();
    private RequestQueue requestQueue;
    private DBHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_joke_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHandler = new DBHandler(requireContext());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        requestQueue = mySingleton.getInstance(requireContext()).getRequestQueue();

        fetchFirstJoke(() -> {
            if (!isAdded()) {
                return;
            }

            JokePagerAdapter adapter = new JokePagerAdapter(requireActivity(), jokes);
            viewPager.setAdapter(adapter);
        });
    }

    private void fetchFirstJoke(Runnable onComplete) {
        String url = "https://icanhazdadjoke.com/";

        JsonObjectRequest jokeRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                response -> {
                    String joke = response.optString("joke", "No joke found");
                    jokes.add(joke);
                    if (!isAdded()) {
                        return;
                    }

                    onComplete.run();
                },
                error -> {
                    jokes.add("Error loading joke :(");
                    onComplete.run();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("User-Agent", "https://github.com/mcx360/CS4084-group_18");
                return params;
            }
        };

        requestQueue.add(jokeRequest);
    }

}

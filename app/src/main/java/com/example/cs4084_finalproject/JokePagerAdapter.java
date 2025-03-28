package com.example.cs4084_finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class JokePagerAdapter extends FragmentStateAdapter {

    private final List<String> jokes;
    private final RequestQueue requestQueue;

    public JokePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> initialJokes) {
        super(fragmentActivity);
        this.jokes = new ArrayList<>(initialJokes);
        requestQueue = mySingleton.getInstance(fragmentActivity).getRequestQueue();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Load more jokes when user reaches near the end
        if (position >= jokes.size() - 2) {
            fetchNewJoke();
        }

        return JokeFragment.newInstance(jokes.get(position));
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    private void fetchNewJoke() {
        String url = "https://icanhazdadjoke.com/";

        JsonObjectRequest jokeRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                response -> {
                    String joke = response.optString("joke", "No joke found");
                    jokes.add(joke);
                    notifyItemInserted(jokes.size() - 1);
                },
                error -> {
                    jokes.add("Error loading joke :(");
                    notifyItemInserted(jokes.size() - 1);
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
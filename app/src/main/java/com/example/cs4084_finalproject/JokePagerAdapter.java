package com.example.cs4084_finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class JokePagerAdapter extends FragmentStateAdapter {

    private List<String> jokes = new ArrayList<>();
    private RequestQueue requestQueue;

    public JokePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        requestQueue = mySingleton.getInstance(fragmentActivity).getRequestQueue();
        fetchNewJoke();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position >= jokes.size() - 1) {
            fetchNewJoke();
        }

        String jokeText = position < jokes.size() ? jokes.get(position) : "Loading joke...";
        return JokeFragment.newInstance(jokeText);
    }

    @Override
    public int getItemCount() {
        return Math.max(jokes.size(), 1);
    }

    private void fetchNewJoke() {
        String url = "https://icanhazdadjoke.com/";

        JsonObjectRequest jokeRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String joke = response.optString("joke", "No joke found");
                        jokes.add(joke);
                        notifyItemInserted(jokes.size() - 1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jokes.add("Error loading joke :(");
                        notifyItemInserted(jokes.size() - 1);
                    }
                }) {
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
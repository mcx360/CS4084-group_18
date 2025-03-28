package com.example.cs4084_finalproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeJokeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<String> jokes = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_joke_activity);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        requestQueue = mySingleton.getInstance(this).getRequestQueue();

        // Fetch the first joke before setting up the adapter
        fetchFirstJoke(() -> {
            JokePagerAdapter adapter = new JokePagerAdapter(HomeJokeActivity.this, jokes);
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
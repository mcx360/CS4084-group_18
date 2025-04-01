package com.example.cs4084_finalproject;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeJokeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<String> jokes = new ArrayList<>();
    private RequestQueue requestQueue;
    private ImageButton likeBtn;
    private DBHandler dbHandler = new DBHandler(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

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

    //called when like button is clicked, saves jokes to local database
    public void saveJoke(View view){
        TextView jokeText = findViewById(R.id.jokeText);
        String joke = jokeText.getText().toString();
        dbHandler.addNewJoke(joke);
        Toast.makeText(HomeJokeActivity.this, "joke saved", Toast.LENGTH_SHORT).show();
    }

}
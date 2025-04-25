package com.example.cs4084_finalproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeJokeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<String> jokes = new ArrayList<>();
    private RequestQueue requestQueue;
    private DBHandler dbHandler = new DBHandler(this);

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        setTopBarTitle("Laughs");
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        return true;
    }
     */
    /*
    public void setTopBarTitle(String title){
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.orange));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_joke_fragment);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
package com.example.cs4084_finalproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeMemeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RequestQueue requestQueue;
    private MemePagerAdapter adapter;
    private List<String> memeUrls = new ArrayList<>();
    private DBHandler dbHandler = new DBHandler(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        setTopBarTitle("Laughs");
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_meme_activity);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        requestQueue = mySingleton.getInstance(this).getRequestQueue();

        // Load first meme before adapter
        fetchFirstMemes(() -> {
            adapter = new MemePagerAdapter(HomeMemeActivity.this, memeUrls);
            viewPager.setAdapter(adapter);
        });
    }

    private void fetchFirstMemes(Runnable onComplete) {
        final AtomicInteger loadedCount = new AtomicInteger(0);

        for (int i = 0; i < 3; i++) {
            String url = "https://memeapi.zachl.tech/pic/json";

            JsonObjectRequest memeRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    response -> {
                        String memeUrl = extractMemeUrl(response);
                        memeUrls.add(memeUrl);
                        if (loadedCount.incrementAndGet() == 3) {
                            onComplete.run();
                        }
                    },
                    error -> {
                        memeUrls.add(null);
                        if (loadedCount.incrementAndGet() == 3) {
                            onComplete.run();
                        }
                    }
            );
            requestQueue.add(memeRequest);
        }
    }

    private String extractMemeUrl(JSONObject jsonObject) {
        String input = jsonObject.optString("MemeURL", "noMemeReturned");
        int index = input.indexOf('?');
        return index != -1 ? input.substring(0, index) : input;
    }

    //not currently fully working as memeURLs are limited to size3
    public void saveMeme(View view){
        int index = viewPager.getCurrentItem();
            String memeURL = adapter.getMemeUrls().get(index);
            dbHandler.addNewMeme(memeURL);
            Toast.makeText(HomeMemeActivity.this, "Meme saved", Toast.LENGTH_SHORT).show();
            Log.i("memeSaved", memeURL);
    }

}
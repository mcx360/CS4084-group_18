package com.example.cs4084_finalproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeMemeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RequestQueue requestQueue;
    private List<String> memeUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_meme_activity);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        requestQueue = mySingleton.getInstance(this).getRequestQueue();

        // Load first meme before adapter
        fetchFirstMemes(() -> {
            MemePagerAdapter adapter = new MemePagerAdapter(HomeMemeActivity.this, memeUrls);
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
}
package com.example.cs4084_finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class HomeMemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_meme_activity);
    }

    public String extractMeme(JSONObject jsonObject) {
        String input = jsonObject.optString("MemeURL", "noMemeReturned");
        int index = input.indexOf('?');
        if (index != -1) {
            return input.substring(0, index);
        }
        return input;
    }

    public void getMeme(View view) {
        int max_width = 300, max_height = 300;
        String url = "https://memeapi.zachl.tech/pic/json";
        ImageView memeView = findViewById(R.id.memeView);

        JsonObjectRequest memeRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String memeURL = extractMeme(response);
                Log.e("memeURL", memeURL);

                if (memeView == null) {
                    Log.e("gettingMeme", "Imageview not found");
                    return;
                }

                ImageRequest imageRequest = new ImageRequest(memeURL, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        memeView.setImageBitmap(response);
                        Log.i("memeView", "fetched");
                    }
                }, max_width, max_height, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("gettingMeme", "could not get the meme :(");
                    }
                });
                mySingleton.getInstance(view.getContext()).addToRequestQueue(imageRequest);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("memeFetch", "failed to fetch meme :(");

            }
        });
        mySingleton.getInstance(this).addToRequestQueue(memeRequest);
    }
}
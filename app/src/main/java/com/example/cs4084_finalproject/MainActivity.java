package com.example.cs4084_finalproject;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = com.example.cs4084_finalproject.mySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

    }

    public void getJoke(View view) {
        TextView textView = (TextView) findViewById(R.id.joke);

        String url = "https://icanhazdadjoke.com/";

        JsonObjectRequest jokeRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String joke = extractJoke(response);
                Log.i("Joke", joke);
                textView.setText(joke);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Error occured :(");
            }
        }) {
            @Override
            //headers for HTTP request that indicate we only accept HTTP responses and User-agent header used for API provider to be able to identify users of API
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("User-Agent", "https://github.com/mcx360/CS4084-group_18");

                return params;
            }
        };
        com.example.cs4084_finalproject.mySingleton.getInstance(this).addToRequestQueue(jokeRequest);

    }

    //Extract joke from Json object and make it a String
    public static String extractJoke(JSONObject jsonObject) {
        return jsonObject.optString("joke", "No joke found");
    }


}
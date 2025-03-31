package com.example.cs4084_finalproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemePagerAdapter extends FragmentStateAdapter {
    private final List<String> memeUrls;
    private final RequestQueue requestQueue;

    public MemePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> initialMemes) {
        super(fragmentActivity);
        this.memeUrls = new ArrayList<>(initialMemes);
        this.requestQueue = mySingleton.getInstance(fragmentActivity).getRequestQueue();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position >= memeUrls.size() - 3) {
            fetchNewMeme();
        }
        return MemeFragment.newInstance(memeUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return memeUrls.size();
    }

    private void fetchNewMeme() {
        String url = "https://memeapi.zachl.tech/pic/json";

        JsonObjectRequest memeRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    String memeUrl = extractMemeUrl(response);
                    memeUrls.add(memeUrl);
                    notifyItemInserted(memeUrls.size() - 1);
                },
                error -> {
                    memeUrls.add(null);
                    notifyItemInserted(memeUrls.size() - 1);
                }
        );

        requestQueue.add(memeRequest);
    }

    private String extractMemeUrl(JSONObject jsonObject) {
        String input = jsonObject.optString("MemeURL", "noMemeReturned");
        int index = input.indexOf('?');
        return index != -1 ? input.substring(0, index) : input;
    }

    public List<String> getMemeUrls(){
        return memeUrls;
    }
}
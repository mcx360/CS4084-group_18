package com.example.cs4084_finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeMemeFragment extends Fragment {

    private ViewPager2 viewPager;
    private RequestQueue requestQueue;
    private MemePagerAdapter adapter;
    private List<String> memeUrls = new ArrayList<>();
    private DBHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_meme_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHandler = new DBHandler(requireContext());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        requestQueue = mySingleton.getInstance(requireContext()).getRequestQueue();

        fetchFirstMemes(() -> {
            if (!isAdded()) {
                return;
            }

            adapter = new MemePagerAdapter(requireActivity(), memeUrls);
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
                            if (!isAdded()) {
                                return;
                            }

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

    private String extractMemeUrl(org.json.JSONObject jsonObject) {
        String input = jsonObject.optString("MemeURL", "noMemeReturned");
        int index = input.indexOf('?');
        return index != -1 ? input.substring(0, index) : input;
    }

    public void saveMeme(View view) {
        int index = viewPager.getCurrentItem();
        String memeURL = adapter.getMemeUrls().get(index);
        dbHandler.addNewMeme(memeURL);
        Toast.makeText(requireContext(), "Meme saved", Toast.LENGTH_SHORT).show();
        Log.i("memeSaved", memeURL);
    }
}

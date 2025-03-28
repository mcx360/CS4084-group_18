package com.example.cs4084_finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class HomeJokeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_joke_activity);

        viewPager = findViewById(R.id.viewPager);

        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        JokePagerAdapter adapter = new JokePagerAdapter(this);
        viewPager.setAdapter(adapter);

    }
}
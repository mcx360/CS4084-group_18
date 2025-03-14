package com.example.cs4084_finalproject;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeJokeActivity extends AppCompatActivity {

    private View tapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_joke_activity);

        tapView = findViewById(R.id.tapToContinueView);

        tapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    loadNextMemeFragment();
                }
                return true;
            }
        });
    }

    private void loadNextMemeFragment() {
        Fragment memeFragment = new MemeFragment();

        //TO DO
    }
}

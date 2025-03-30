package com.example.cs4084_finalproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cs4084_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sets default fragment on startup
        replaceFragment(new HomeFragment());

        // Make BottomNavigationView background transparent
        binding.bottomNavigationView.setBackgroundResource(android.R.color.transparent);

        // BottomNavigationView listener
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.jokes) {
                selectedFragment = new JokeFragment();
            } else if (itemId == R.id.memes) {
                selectedFragment = new MemeFragment();
            } else if (itemId == R.id.account) {
                selectedFragment = new AccountFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // Upload button (island-style FAB)
        binding.fabUpload.setOnClickListener(v -> {
            replaceFragment(new UploadFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
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
        replaceFragment(new JokesFragment());

        binding.bottomNavigationView.setSelectedItemId(R.id.jokes);


        // Make BottomNavigationView background transparent
        binding.bottomNavigationView.setBackgroundResource(android.R.color.transparent);

        // BottomNavigationView listener
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.saved_jokes) {
                selectedFragment = new SavedJokesFragment();
            } else if (itemId == R.id.jokes) {
                selectedFragment = new JokesFragment();
            } else if (itemId == R.id.memes) {
                selectedFragment = new MemesFragment();
            } else if (itemId == R.id.saved_memes) {
                selectedFragment = new SavedMemesFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // Upload button (island-style FAB)
        binding.fabRefresh.setOnClickListener(v -> replaceFragment(new RefreshFragment()));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
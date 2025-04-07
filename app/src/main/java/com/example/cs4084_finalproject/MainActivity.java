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
        replaceFragment(new HomeJokeFragment());

        binding.bottomNavigationView.setSelectedItemId(R.id.jokes);
        binding.topAppBar.setTitle("Jokes");

        // Make BottomNavigationView background transparent
        binding.bottomNavigationView.setBackgroundResource(android.R.color.transparent);

        // BottomNavigationView listener
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.saved_jokes) {
                selectedFragment = new SavedJokesFragment();
                binding.topAppBar.setTitle("Saved Jokes");

            } else if (itemId == R.id.jokes) {
                selectedFragment = new HomeJokeFragment();
                binding.topAppBar.setTitle("Jokes");

            } else if (itemId == R.id.memes) {
                selectedFragment = new HomeMemeFragment();
                binding.topAppBar.setTitle("Memes");

            } else if (itemId == R.id.saved_memes) {
                selectedFragment = new SavedMemesFragment();
                binding.topAppBar.setTitle("Saved Memes");
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // FAB
        binding.fabRefresh.setOnClickListener(v -> {
            replaceFragment(new RefreshFragment());
            binding.topAppBar.setTitle("Refresh");
        });

        // Top app bar buttons (notifications & settings)
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.notifications) {
                replaceFragment(new NotificationsFragment());
                binding.topAppBar.setTitle("Notifications");
                return true;

            } else if (id == R.id.settings) {
                replaceFragment(new SettingsFragment());
                binding.topAppBar.setTitle("Settings");
                return true;
            }

            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

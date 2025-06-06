package com.example.cs4084_finalproject;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.strictmode.IncorrectContextUseViolation;
import android.util.TypedValue;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cs4084_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check dark mode setting
        if (SettingsActivity.isDarkModeEnabled(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        if (!SettingsActivity.isDarkModeEnabled(this)) {
            int iconColor = Color.BLACK;
            binding.topAppBar.setTitleTextColor(iconColor);
            binding.topAppBar.setNavigationIconTint(iconColor);
            binding.topAppBar.setBackgroundColor(Color.WHITE);
            if (binding.topAppBar.getMenu().findItem(R.id.notifications).getIcon() != null) {
                binding.topAppBar.getMenu().findItem(R.id.notifications).getIcon().setTint(iconColor);
            }
            if (binding.topAppBar.getMenu().findItem(R.id.settings).getIcon() != null) {
                binding.topAppBar.getMenu().findItem(R.id.settings).getIcon().setTint(iconColor);
            }
        } else {
            int iconColor = Color.WHITE;
            binding.topAppBar.setTitleTextColor(iconColor);
            binding.topAppBar.setNavigationIconTint(iconColor);
            binding.topAppBar.setBackgroundColor(Color.DKGRAY);
            if (binding.topAppBar.getMenu().findItem(R.id.notifications).getIcon() != null) {
                binding.topAppBar.getMenu().findItem(R.id.notifications).getIcon().setTint(iconColor);
            }
            if (binding.topAppBar.getMenu().findItem(R.id.settings).getIcon() != null) {
                binding.topAppBar.getMenu().findItem(R.id.settings).getIcon().setTint(iconColor);
            }
        }



        // Sets default fragment on startup
        replaceFragment(new HomeJokeFragment());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean isGranted = ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;

            if (isGranted) {
                binding.topAppBar.getMenu().findItem(R.id.notifications).setVisible(false);
            }
        }

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
                requestNotificationPermission();
                return true;

            } else if (id == R.id.settings) {
              Intent intent = new Intent(this, SettingsActivity.class);
              startActivity(intent);
                return true;
            }

            return false;
        });


    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission denied
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitAllowingStateLoss();
    }
}

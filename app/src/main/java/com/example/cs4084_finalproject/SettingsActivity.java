package com.example.cs4084_finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.switchmaterial.SwitchMaterial;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.work.WorkManager;
import androidx.appcompat.app.AppCompatDelegate;


import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SwitchMaterial darkModeSwitch = findViewById(R.id.switch2);
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        darkModeSwitch.setChecked(currentMode == AppCompatDelegate.MODE_NIGHT_YES);

        SwitchMaterial notificationSwitch = findViewById(R.id.switch3);
        boolean notificationsEnabled = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("notifications", true); // true by default
        notificationSwitch.setChecked(notificationsEnabled);
    }
public void darkMode(View view){
    SwitchMaterial darkModeSwitch = findViewById(R.id.switch2);
    boolean isChecked = darkModeSwitch.isChecked();
    getSharedPreferences("settings", MODE_PRIVATE)
            .edit()
            .putBoolean("dark_mode", isChecked)
            .apply();
        if(isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate();
}

public static boolean isDarkModeEnabled(Context context){

     return  context.getSharedPreferences("settings",context.MODE_PRIVATE).getBoolean("dark_mode",false);
}
    public void cancelNotification(View view){
     boolean isChecked = ((SwitchMaterial) (findViewById(R.id.switch3))).isChecked();
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("notifications", isChecked)
                .apply();
        if(!isChecked) {
            WorkManager.getInstance(getApplicationContext()).cancelAllWorkByTag("daily_notification");
            Toast.makeText(this, "Notifications turned off", Toast.LENGTH_SHORT).show();

        } else{
            Welcome_Activity.scheduleNotification(getApplicationContext());
            Toast.makeText(this, "Notifications turned on", Toast.LENGTH_SHORT).show();
        }

    }
    public void clearCache(View view) {
       try {
           File dir = getCacheDir();
           deleteDir(dir);
           Toast.makeText(this, "Cache cleared successfully!", Toast.LENGTH_SHORT).show();
       } catch (Exception e){
           e.printStackTrace();
           Toast.makeText(this,"Cache could not be cleared!",Toast.LENGTH_SHORT).show();
       }


    }

    public boolean deleteDir(File dir){
        if (dir!=null && dir.isDirectory()){
            String [] children = dir.list();
            for(String child : children){
                boolean success = deleteDir(new File(dir,child));
                if(!success){
                    return false;
                }
            }
            return dir.delete();
        } else if (dir.isFile()){
          return  dir.delete();

        } else{
            return false;
        }
    }

    public void openAboutPage(View view) {
   Intent intent = new Intent(this,About_Us_Activity.class);
   startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public void backToLaughs(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
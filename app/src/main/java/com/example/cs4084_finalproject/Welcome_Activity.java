package com.example.cs4084_finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Welcome_Activity extends AppCompatActivity {
    private CheckBox checkBox;
    private TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void onContinueButtonClick(View view){
        checkBox = findViewById(R.id.checkBox);
        textView5 = findViewById(R.id.textView5);
        if(checkBox.isChecked()) {
            //if TOS are agreed, then the user will receive notifications from us
            scheduleNotification(getApplicationContext());

            Intent intent = new Intent(this, HomeMemeActivity.class);
            startActivity(intent);
        }
        else{
           textView5.setVisibility(View.VISIBLE);
        }
    }

    //daily notifications
    public static void scheduleNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Calculate delay
        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        // If the time has already passed today, schedule for tomorrow at the same time
        if (delay <= 0) {
            delay += TimeUnit.DAYS.toMillis(1); // Add 24 hours to schedule for tomorrow
        }

        PeriodicWorkRequest notificationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS) // Set initial delay to the calculated delay
                .addTag("daily_notification")
                .build();

        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
    }
}

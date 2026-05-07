package com.example.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "assignment_channel_high";
    private Button btnNotify;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showNotification();
                } else {
                    Toast.makeText(this, "Permission denied. Cannot show notification.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btnNotify);
        createNotificationChannel();

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) ==
                            PackageManager.PERMISSION_GRANTED) {
                        showNotification();
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    }
                } else {
                    showNotification();
                }
            }
        });
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Dummy intents for actions as seen in the assignment image
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        PendingIntent emailPendingIntent = PendingIntent.getActivity(this, 0, emailIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("The Big Meeting")
                .setContentText("4:15 - 5:15 PM\nBig Conference Room")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority for heads-up
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_dialog_map, "MAP", mapPendingIntent)
                .addAction(android.R.drawable.ic_dialog_email, "EMAIL GUESTS", emailPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL); // Enable sound and vibration for heads-up

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Assignment Channel High";
            String description = "Channel for heads-up notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Importance high for heads-up
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
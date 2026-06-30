package com.example.chalarm;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AlarmScreenActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private TextView promptText;
    private TextView statusText;
    private Button captureButton;
    private Button emergencyButton;

    private ObjectManager objectManager;
    private ObjectManager.ObjectItem selectedObject;
    private Bitmap capturedImage;
    private AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        // Keep screen on and show over lock screen
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        // Disable lock screen
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager != null) {
            keyguardManager.requestDismissKeyguard(this, null);
        }

        objectManager = new ObjectManager(this);
        selectRandomObject();
        initializeViews();
        setupListeners();
    }

    private void selectRandomObject() {
        selectedObject = objectManager.getRandomObject();
        if (selectedObject == null) {
            Toast.makeText(this, "No objects registered!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        promptText = findViewById(R.id.promptText);
        statusText = findViewById(R.id.statusText);
        captureButton = findViewById(R.id.captureButton);
        emergencyButton = findViewById(R.id.emergencyButton);

        if (selectedObject != null) {
            promptText.setText("Go take a picture of:\n" + selectedObject.label);
        }
    }

    private void setupListeners() {
        captureButton.setOnClickListener(v -> openCamera());
        emergencyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmergencyPinActivity.class);
            startActivity(intent);
        });
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            capturedImage = (Bitmap) data.getExtras().get("data");
            checkImageMatch();
        }
    }

    private void checkImageMatch() {
        if (selectedObject == null || selectedObject.image == null) {
            statusText.setText("Error: Reference image missing");
            return;
        }

        boolean matches = ImageMatcher.matchImages(capturedImage, selectedObject.image);

        if (matches) {
            statusText.setText("✓ Match found! Alarm dismissed.");
            dismissAlarm();
        } else {
            statusText.setText("✗ No match! Try again (use better lighting)");
        }
    }

    private void dismissAlarm() {
        alarmService = new AlarmService();
        alarmService.stopAlarm();
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent back button from dismissing alarm
        // Do nothing
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}
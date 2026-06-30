package com.example.chalarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddObjectActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private ImageView imagePreview;
    private EditText labelInput;
    private Button captureButton;
    private Button saveButton;

    private Bitmap capturedImage;
    private ObjectManager objectManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        objectManager = new ObjectManager(this);

        if (!objectManager.canAddObject()) {
            Toast.makeText(this, "Maximum objects reached (15)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        imagePreview = findViewById(R.id.imagePreview);
        labelInput = findViewById(R.id.labelInput);
        captureButton = findViewById(R.id.captureButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupListeners() {
        captureButton.setOnClickListener(v -> openCamera());
        saveButton.setOnClickListener(v -> saveObject());
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
            imagePreview.setImageBitmap(capturedImage);
            imagePreview.setVisibility(View.VISIBLE);
            saveButton.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveObject() {
        if (capturedImage == null) {
            Toast.makeText(this, "Please capture an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        String label = labelInput.getText().toString().trim();
        if (label.isEmpty()) {
            Toast.makeText(this, "Please enter a label", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            objectManager.addObject(label, capturedImage);
            Toast.makeText(this, "Object added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
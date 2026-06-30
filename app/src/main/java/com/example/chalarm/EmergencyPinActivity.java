package com.example.chalarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyPinActivity extends AppCompatActivity {
    private static final String EMERGENCY_PIN = "1234"; // You should store this securely
    private EditText pinInput;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_pin);

        pinInput = findViewById(R.id.pinInput);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(v -> checkPin());
    }

    private void checkPin() {
        String pin = pinInput.getText().toString().trim();
        if (pin.equals(EMERGENCY_PIN)) {
            Toast.makeText(this, "PIN correct. Dismissing alarm.", Toast.LENGTH_SHORT).show();
            dismissAlarm();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
        }
    }

    private void dismissAlarm() {
        AlarmService alarmService = new AlarmService();
        alarmService.stopAlarm();
        finish();
    }
}
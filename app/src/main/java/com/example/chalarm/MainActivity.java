package com.example.chalarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ObjectManager objectManager;
    private AlarmManagerHelper alarmHelper;
    private TimePicker timePicker;
    private TextView statusText;
    private TextView objectCountText;
    private Button setAlarmButton;
    private Button addObjectButton;
    private Button viewObjectsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectManager = new ObjectManager(this);
        alarmHelper = new AlarmManagerHelper(this);

        initializeViews();
        updateUI();


        setAlarmButton.setOnClickListener(v -> setAlarm());
        addObjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddObjectActivity.class);
            startActivity(intent);
        });
        viewObjectsButton.setOnClickListener(v -> showObjects());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initializeViews() {
        timePicker = findViewById(R.id.timePicker);
        statusText = findViewById(R.id.statusText);
        objectCountText = findViewById(R.id.objectCountText);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        addObjectButton = findViewById(R.id.addObjectButton);
        viewObjectsButton = findViewById(R.id.viewObjectsButton);
    }

    private void updateUI() {
        int count = objectManager.getObjectCount();
        objectCountText.setText("Objects: " + count + "/15");

        if (objectManager.canSetAlarm()) {
            statusText.setText("✓ Ready to set alarm!");
            setAlarmButton.setEnabled(true);
        } else {
            statusText.setText("⚠️ Add " + (5 - count) + " more objects to enable alarm");
            setAlarmButton.setEnabled(false);
        }

        addObjectButton.setEnabled(objectManager.canAddObject());
    }

    private void setAlarm() {
        if (!objectManager.canSetAlarm()) {
            Toast.makeText(this, "Need at least 5 objects!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!alarmHelper.canScheduleExactAlarm()) {
            alarmHelper.openAlarmSettings();
            Toast.makeText(this, "Please allow exact alarm permission", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmHelper.scheduleAlarm(calendar);
        Toast.makeText(this, "Alarm set for " +
                        String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute()),
                Toast.LENGTH_SHORT).show();
    }

    private void showObjects() {
        // Implement object list display
        Toast.makeText(this, "Objects: " + objectManager.getObjectCount(), Toast.LENGTH_SHORT).show();
    }
}
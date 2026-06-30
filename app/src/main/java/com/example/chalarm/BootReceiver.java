package com.example.chalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null &&
                intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Reschedule alarms if needed
            // You should store alarm time in SharedPreferences and reschedule here
        }
    }
}
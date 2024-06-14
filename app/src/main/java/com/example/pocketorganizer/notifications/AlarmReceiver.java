package com.example.pocketorganizer.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String noteTitle = intent.getStringExtra("noteTitle");
        String time = intent.getStringExtra("time");
        int noteId = intent.getIntExtra("noteId", -1);
        NotificationHelper.showNotification(context, noteId, noteTitle, time);
    }
}

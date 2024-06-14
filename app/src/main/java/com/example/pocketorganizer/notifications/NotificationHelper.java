package com.example.pocketorganizer.notifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.pocketorganizer.R;

import java.text.SimpleDateFormat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "NOTE_NOTIFICATION";

    public static void createNotificationChannel(Context context) {
        CharSequence name = "Уведомление о заметках";
        String description = "Заметки, с напоминанием по времени";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent createPendingIntent(Context context, int noteId, String noteTitle, String time) {
        Intent intent = new Intent(context, AlarmReceiver.class); // Intent, указывающий на BroadcastReceiver
        intent.putExtra("noteTitle", noteTitle);
        intent.putExtra("time", time);
        intent.putExtra("noteId", noteId);
        return PendingIntent.getBroadcast(
                context,
                noteId, // используем noteId чтобы PendingIntent был уникальным
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    // Общая функция для установки уведомления
    private static void setAlarm(Context context, int noteId, String noteTitle, Calendar calendar) {
        long triggerAtMillis = calendar.getTimeInMillis();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedTime = dateFormat.format(calendar.getTime());

        PendingIntent pendingIntent = createPendingIntent(context, noteId, noteTitle, formattedTime);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
            Intent requestIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            context.startActivity(requestIntent);
            return;
        }

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    public static void cancelScheduledNotification(Context context, int noteId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createPendingIntent(context, noteId, "", "");

        // Отмена уведомления
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    // Функция для запланирования нового уведомления
    public static void scheduleNotification(Context context, int noteId, String noteTitle, Calendar calendar) {
        setAlarm(context, noteId, noteTitle, calendar);
    }

    // Функция для обновления существующего уведомления
    public static void updateScheduledNotification(Context context, int noteId, String noteTitle, Calendar newCalendar) {
        // Отмена существующего уведомления
        cancelScheduledNotification(context, noteId);

        // Установка нового уведомления
        setAlarm(context, noteId, noteTitle, newCalendar);
    }

    public static void showNotification(Context context, int noteId, String noteTitle, String time) {
        if (noteId != -1) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Разрешение на уведомления отключены", Toast.LENGTH_SHORT).show();
                return;
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.check_circle_blue)
                    .setContentTitle(noteTitle)
                    .setContentText(time)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{0, 1000});

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(noteId, builder.build());
        }
    }
}

package com.example.pocketorganizer.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.database.AppDatabase;


public class SettingsActivity extends AppCompatActivity {

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle("Удаление всех заметок")
                .setMessage("Вы уверены, что хотите удалить все заметки?")
                .setPositiveButton("Удалить", (dialog, which) -> deleteAllNotes())
                .setNegativeButton("Отменить", null)
                .show();
    }

    private void deleteAllNotes(){
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        new Thread(() -> database.noteDao().deleteAllNotes()).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Определение темы приложения
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingsPrefs", 0);

        boolean isDarkTheme = sharedPreferences.getBoolean("DarkTheme", true);
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_settings);

        //Обработка Switch
        SwitchCompat themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(isDarkTheme);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("DarkTheme", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        //Обработка кнопки удаления всех заметок
        Button settingsButton = findViewById(R.id.deleteAllNotesButton);
        settingsButton.setOnClickListener(view -> showDeleteConfirmationDialog());

        //Обработка кнопки назад
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
    }
}
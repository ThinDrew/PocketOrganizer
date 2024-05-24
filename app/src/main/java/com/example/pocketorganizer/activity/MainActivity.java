package com.example.pocketorganizer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketorganizer.CalendarHelper;
import com.example.pocketorganizer.DayOfWeek;
import com.example.pocketorganizer.DayOfWeekAdapter;
import com.example.pocketorganizer.R;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DayOfWeekAdapter adapter;
    private TextView monthTextView;
    private CalendarHelper calendarHelper;
    private AppDatabase database;

    protected void displayWeek(){
        new Thread(() -> {
            List<DayOfWeek> week = calendarHelper.getWeek();
            for(int i = 0; i < calendarHelper.getWeekSize(); i++){
                List<Note> lst = database.noteDao().getNotesForDate(week.get(i).getFormattedDate());
                week.get(i).setNotes((ArrayList)lst);
            }

            runOnUiThread(() -> {
                adapter.updateData(week);
                monthTextView.setText(calendarHelper.getMonthText());
            });
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация объектов
        calendarHelper = new CalendarHelper();
        database = AppDatabase.getInstance(getApplicationContext());

        // Инициализация Views
        recyclerView = findViewById(R.id.weekRecyclerView);
        monthTextView = findViewById(R.id.monthText);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DayOfWeekAdapter(calendarHelper.getWeek(), database);
        recyclerView.setAdapter(adapter);

        // Отображение текущей недели
        displayWeek();

        // Обработка кнопок навигации
        ImageButton previousWeekButton = findViewById(R.id.prevWeekButton);
        previousWeekButton.setOnClickListener(v -> {
            calendarHelper.getPreviousWeek();
            displayWeek();
        });

        ImageButton nextWeekButton = findViewById(R.id.nextWeekButton);
        nextWeekButton.setOnClickListener(v -> {
            calendarHelper.getNextWeek();
            displayWeek();
        });

        //Обработка кнопки аккаунта/настроек
        ImageButton settingsButton = findViewById(R.id.accountButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}

package com.example.pocketorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketorganizer.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DayOfWeekAdapter adapter;
    private TextView monthTextView;
    private CalendarHelper calendarHelper;
    private AppDatabase database;

    protected void displayWeek() {
        // Получение текущей недели и обновление данных в адаптере
        adapter.updateData(calendarHelper.getWeek()); // Предполагается, что у вас есть метод updateData в адаптере
        // Обновление текущего месяца и года в TextView
        monthTextView.setText(calendarHelper.getMonthText());
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

        // Настройка кнопок навигации
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
    }
}

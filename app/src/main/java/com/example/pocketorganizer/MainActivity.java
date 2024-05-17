package com.example.pocketorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketorganizer.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    protected void displayWeek(CalendarHelper calendarHelper, AppDatabase database){
        //Получение текущей недели и преобразование её в список элементов для RecyclerView
        RecyclerView recyclerView = findViewById(R.id.weekRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DayOfWeekAdapter adapter = new DayOfWeekAdapter(calendarHelper.getWeek(), database);
        recyclerView.setAdapter(adapter);
        //Получение текущего месяца и года и обновление данных в TextView
        TextView monthTextView = findViewById(R.id.monthText);
        monthTextView.setText(calendarHelper.getMonthText());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarHelper calendarHelper = new CalendarHelper();
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());

        displayWeek(calendarHelper, database);

        ImageButton previousWeekButton = findViewById(R.id.prevWeekButton);
        previousWeekButton.setOnClickListener(v -> {
            calendarHelper.getPreviousWeek();
            displayWeek(calendarHelper, database);
        });

        ImageButton nextWeekButton = findViewById(R.id.nextWeekButton);
        nextWeekButton.setOnClickListener(v -> {
            calendarHelper.getNextWeek();
            displayWeek(calendarHelper, database);
        });
    }
}
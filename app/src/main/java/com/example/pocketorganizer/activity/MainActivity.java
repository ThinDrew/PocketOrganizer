package com.example.pocketorganizer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketorganizer.CalendarHelper;
import com.example.pocketorganizer.DayOfWeek;
import com.example.pocketorganizer.adapter.DayOfWeekAdapter;
import com.example.pocketorganizer.R;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.model.Note;
import com.example.pocketorganizer.notifications.NotificationHelper;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_EDIT_NOTE = 1;
    public static final int REQUEST_CODE_SETTINGS = 2;
    private RecyclerView recyclerView;
    private DayOfWeekAdapter adapter;
    private TextView dateTextView;
    private CalendarHelper calendarHelper;
    private AppDatabase database;

    protected void displayWeek(){
        new Thread(() -> {
            List<DayOfWeek> week = calendarHelper.getWeek();
            for(int i = 0; i < calendarHelper.getWeekSize(); i++){
                List<Note> lst = database.noteDao().getNotesForDate(week.get(i).getDate().toString());
                week.get(i).setNotes((ArrayList<Note>)lst);
            }

            List<Note> somedayNotes = database.noteDao().getNotesForDate("Someday");

            runOnUiThread(() -> {
                adapter.updateData(week, somedayNotes);
                dateTextView.setText(calendarHelper.getMonthText());
            });
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHelper.createNotificationChannel(this);

        // Инициализация объектов
        calendarHelper = new CalendarHelper();
        database = AppDatabase.getInstance(getApplicationContext());

        // Инициализация Views
        recyclerView = findViewById(R.id.weekRecyclerView);
        dateTextView = findViewById(R.id.dateTextView);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DayOfWeekAdapter(calendarHelper.getWeek());
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

        // Обработка кнопки аккаунта/настроек
        ImageButton settingsButton = findViewById(R.id.accountButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        });

        // Обработка нажатия на TextView с датой
        dateTextView.setOnClickListener(v -> {
            // Создаем MaterialDatePicker
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите дату")
                    .setTheme(R.style.CustomMaterialDatePicker)
                    .build();

            // Показываем DatePicker
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");

            // Добавляем обработчик для выбранной даты
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                // 'selection' хранит выбранную дату в миллисекундах от эпохи
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                calendarHelper.setWeekByDate(day, month, year);
                displayWeek();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Если закрылось окно настроек
        if (requestCode == REQUEST_CODE_SETTINGS) {
            displayWeek();
        }
        //Если закрылось окно редактора заметки
        else if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK) {
            displayWeek();
        }
    }
}

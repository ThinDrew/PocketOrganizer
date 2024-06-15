package com.example.pocketorganizer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.adapter.ToDoItemAdapter;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.model.Note;
import com.example.pocketorganizer.model.ToDoItem;
import com.example.pocketorganizer.notifications.NotificationHelper;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private boolean isEditMode = false;
    private Note currentNote;
    private List<ToDoItem> toDoList;
    private ToDoItemAdapter toDoItemAdapter;
    private int newCount = -1;
    private boolean isListVisible = true;
    private String timeText;
    private int notificationHour;
    private int notificationMinute;
    private ImageButton deleteNotificationButton;
    private TextView timeInput;
    private ImageButton showListButton;
    private RecyclerView recyclerView;

    private enum NotificationStatus {
        NOTIFICATION_NULL,
        NOTIFICATION_EXISTED,
        NOTIFICATION_ADDED,
        NOTIFICATION_CHANGED,
        NOTIFICATION_DELETED
    }
    private NotificationStatus notificationStatus = NotificationStatus.NOTIFICATION_NULL;

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle("Удаление заметки")
                .setMessage("Вы уверены, что хотите удалить эту заметку?")
                .setPositiveButton("Удалить", (dialog, which) -> deleteNote())
                .setNegativeButton("Отменить", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteNoteButton);
        ImageButton addToDoItemButton = findViewById(R.id.addToDoItemButton);
        showListButton = findViewById(R.id.showListButton);
        timeInput = findViewById(R.id.timeInputTextView);
        deleteNotificationButton = findViewById(R.id.deleteNotificationButton);
        deleteNotificationButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        String noteDate = intent.getStringExtra("noteDate");
        if (Objects.equals(noteDate, "Someday")) {
            findViewById(R.id.notificationLayout).setVisibility(View.GONE);
        }

        toDoList = new ArrayList<>();
        recyclerView = findViewById(R.id.toDoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showList();

        //Если мы открываем это Activity для редактирования заметки
        if (intent.hasExtra("note_id")) {
            isEditMode = true;
            int noteId = intent.getIntExtra("note_id", -1);
            new Thread(() -> {
                AppDatabase database = AppDatabase.getInstance(this);
                currentNote = database.noteDao().getById(noteId);
                toDoList = database.noteDao().getNoteWithTodos(noteId).toDoItems;

                runOnUiThread(() -> {
                    if (currentNote != null) {
                        if (currentNote.getNotificationTime() != null && currentNote.getNotificationTime().length() == 5){
                            timeText = currentNote.getNotificationTime();
                            notificationStatus = NotificationStatus.NOTIFICATION_EXISTED;
                            timeInput.setText("Напомнить в " + timeText);
                            deleteNotificationButton.setVisibility(View.VISIBLE);
                        }

                        if (Objects.equals(currentNote.getDate(), "Someday")) {
                            findViewById(R.id.notificationLayout).setVisibility(View.GONE);
                        }

                        titleEditText.setText(currentNote.getTitle());
                        descriptionEditText.setText(currentNote.getDescription());
                        toDoItemAdapter = new ToDoItemAdapter(this, toDoList);
                        recyclerView.setAdapter(toDoItemAdapter);
                    }
                });
            }).start();
        }
        //Если мы открываем это Activity для добавления заметки
        else {
            currentNote = new Note();
            currentNote.setDate(noteDate);
            currentNote.setChecked(false);
            deleteButton.setVisibility(View.GONE);

            toDoList = new ArrayList<>();
            toDoItemAdapter = new ToDoItemAdapter(this, toDoList);
            recyclerView.setAdapter(toDoItemAdapter);
        }

        // Обработка кнопок
        saveButton.setOnClickListener(view -> saveNote());
        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
        addToDoItemButton.setOnClickListener(view -> addToDoItem());
        showListButton.setOnClickListener(view -> showList());
        // Работа с уведомлениями
        timeInput.setOnClickListener(view -> pickTime());
        deleteNotificationButton.setOnClickListener(view -> deleteNotification());
    }

    private void pickTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Выберите время")
                .setTheme(R.style.CustomTimePickerTheme);

        final MaterialTimePicker materialTimePicker = builder.build();

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedHour = materialTimePicker.getHour();
                int selectedMinute = materialTimePicker.getMinute();
                String hourText = selectedHour < 10 ? "0" + selectedHour : String.valueOf(selectedHour);
                String minuteText = selectedMinute < 10 ? "0" + selectedMinute : String.valueOf(selectedMinute);
                String timeText = hourText + ":" + minuteText;
                timeInput.setText("Напомнить в " + timeText);

                notificationHour = selectedHour;
                notificationMinute = selectedMinute;

                if (notificationStatus == NotificationStatus.NOTIFICATION_NULL) {
                    notificationStatus = NotificationStatus.NOTIFICATION_ADDED;
                } else if (notificationStatus != NotificationStatus.NOTIFICATION_ADDED) {
                    notificationStatus = NotificationStatus.NOTIFICATION_CHANGED;
                }

                deleteNotificationButton.setVisibility(View.VISIBLE);
            }
        });

        materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
    }


    private void deleteNotification(){
        deleteNotificationButton.setVisibility(View.GONE);
        if (notificationStatus == NotificationStatus.NOTIFICATION_ADDED)
            notificationStatus = NotificationStatus.NOTIFICATION_NULL;
        else if (notificationStatus == NotificationStatus.NOTIFICATION_CHANGED || notificationStatus == NotificationStatus.NOTIFICATION_EXISTED)
            notificationStatus = NotificationStatus.NOTIFICATION_DELETED;
        timeInput.setText("Введите время...");
    }

    private void addToDoItem() {
        ToDoItem newToDoItem = new ToDoItem();
        newToDoItem.setNoteId(currentNote.getId());
        newToDoItem.setDescription("");
        newToDoItem.setCompleted(false);
        newToDoItem.setId(newCount);
        newCount--;

        toDoList.add(newToDoItem);
        toDoItemAdapter.notifyItemInserted(toDoList.size() - 1);
        toDoItemAdapter.tempToDoItems.add(newToDoItem);

        if (!isListVisible)
            showList();
    }

    private void saveNote() {
        String titleInput = titleEditText.getText().toString();
        String descriptionInput = descriptionEditText.getText().toString();

        final String title = titleInput.isEmpty() ? "Заметка" : titleInput;
        final String description = descriptionInput.isEmpty() ? "Без описания" : descriptionInput;

        currentNote.setTitle(title);
        currentNote.setDescription(description);

        new Thread(() -> {
            int noteId = currentNote.getId();
            AppDatabase database = AppDatabase.getInstance(this);

            if (isEditMode){
                database.noteDao().updateNote(currentNote);
            }
            else{
                noteId = (int)database.noteDao().insertNote(currentNote);
                currentNote.setId(noteId);
            }
            toDoItemAdapter.updateToDoItems();

            // Удаление дел
            List<ToDoItem> deletedToDoItems = toDoItemAdapter.getDeletedToDoItems();
            for (ToDoItem item : deletedToDoItems){
                database.toDoDao().deleteToDoItem(item);
            }

            // Добавление/Обновление дел
            List<ToDoItem> tempToDoItems = toDoItemAdapter.getTempToDoItems();
            for (ToDoItem item : tempToDoItems){
                if (item.getId() < 0){
                    ToDoItem newItem = new ToDoItem();
                    newItem.setCompleted(item.isCompleted());
                    newItem.setDescription(item.getDescription());
                    newItem.setNoteId(noteId);
                    database.toDoDao().insertToDoItem(newItem);
                }
                else
                    database.toDoDao().updateToDoItem(item);
            }

            // Работа с уведомлениями
            if (notificationStatus != NotificationStatus.NOTIFICATION_NULL && notificationStatus != NotificationStatus.NOTIFICATION_EXISTED) {
                // Если строка в формате HH:MM
                if (timeText.length() == 5){
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        // Устанавливаем время для уведомления
                        Date date = dateFormat.parse(currentNote.getDate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
                        calendar.set(Calendar.MINUTE, notificationMinute);

                        switch(notificationStatus){
                            case NOTIFICATION_ADDED:
                                NotificationHelper.scheduleNotification(this, noteId, currentNote.getTitle(), calendar);
                                currentNote.setNotificationTime(timeText);
                                break;
                            case NOTIFICATION_CHANGED:
                                NotificationHelper.updateScheduledNotification(this, noteId, currentNote.getTitle(), calendar);
                                currentNote.setNotificationTime(timeText);
                                break;
                            case NOTIFICATION_DELETED:
                                NotificationHelper.cancelScheduledNotification(this, noteId);
                                currentNote.setNotificationTime(null);
                        }

                        database.noteDao().updateNote(currentNote);

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //Закрываем Activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    private void deleteNote() {
        new Thread(() -> {
            AppDatabase database = AppDatabase.getInstance(this);
            List<ToDoItem> allToDoItems = database.noteDao().getNoteWithTodos(currentNote.getId()).toDoItems;
            for (ToDoItem item : allToDoItems){
                database.toDoDao().deleteToDoItem(item);
            }
            if (currentNote.getNotificationTime() != null){
                NotificationHelper.cancelScheduledNotification(this, currentNote.getId());
            }
            database.noteDao().deleteNote(currentNote);
            //Закрываем Activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }

    private void showList(){
        isListVisible = !isListVisible;

        if (isListVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            showListButton.setBackgroundResource(R.drawable.chevron_down);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            showListButton.setBackgroundResource(R.drawable.chevron_right);
        }
    }
}

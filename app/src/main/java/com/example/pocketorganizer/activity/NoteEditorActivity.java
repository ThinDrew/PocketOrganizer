package com.example.pocketorganizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.adapter.ToDoItemAdapter;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.model.Note;
import com.example.pocketorganizer.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private boolean isEditMode = false;
    private Note currentNote;
    private List<ToDoItem> toDoList;
    private ToDoItemAdapter toDoItemAdapter;
    private int newCount = -1;

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

        Intent intent = getIntent();
        String noteDate = intent.getStringExtra("noteDate");

        toDoList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.toDoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        //Обработка кнопок
        saveButton.setOnClickListener(view -> saveNote());
        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
        addToDoItemButton.setOnClickListener(view -> addToDoItem());
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
            database.noteDao().deleteNote(currentNote);
            //Закрываем Activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }
}

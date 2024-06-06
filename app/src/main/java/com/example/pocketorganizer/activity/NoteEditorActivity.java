package com.example.pocketorganizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketorganizer.R;
import com.example.pocketorganizer.database.AppDatabase;
import com.example.pocketorganizer.model.Note;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private boolean isEditMode = false;
    private Note currentNote;

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

        Intent intent = getIntent();
        String noteDate = intent.getStringExtra("noteDate");

        //Если мы открываем это Activity для редактирования заметки
        if (intent.hasExtra("note_id")) {
            isEditMode = true;
            int noteId = intent.getIntExtra("note_id", -1);
            new Thread(() -> {
                currentNote = AppDatabase.getInstance(this).noteDao().getById(noteId);

                runOnUiThread(() -> {
                    if (currentNote != null) {
                        titleEditText.setText(currentNote.getTitle());
                        descriptionEditText.setText(currentNote.getDescription());
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
        }

        //Обработка кнопок
        saveButton.setOnClickListener(view -> saveNote());
        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void saveNote() {
        String titleInput = titleEditText.getText().toString();
        String descriptionInput = descriptionEditText.getText().toString();

        final String title = titleInput.isEmpty() ? "Заметка" : titleInput;
        final String description = descriptionInput.isEmpty() ? "Без описания" : descriptionInput;

        currentNote.setTitle(title);
        currentNote.setDescription(description);

        new Thread(() -> {
            if (isEditMode) {
                AppDatabase.getInstance(this).noteDao().update(currentNote);
            } else {
                AppDatabase.getInstance(this).noteDao().insertNote(currentNote);
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
            AppDatabase.getInstance(this).noteDao().delete(currentNote);
            //Закрываем Activity
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
            });
        }).start();
    }
}

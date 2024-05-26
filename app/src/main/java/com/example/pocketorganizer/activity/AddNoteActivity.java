package com.example.pocketorganizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketorganizer.R;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private String noteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        noteDate = getIntent().getStringExtra("noteDate");

        saveButton.setOnClickListener(view -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("noteTitle", title);
            resultIntent.putExtra("noteDescription", description);
            resultIntent.putExtra("noteDate", noteDate);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}

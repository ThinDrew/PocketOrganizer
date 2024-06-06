package com.example.pocketorganizer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "note_id")
    public int noteId;

    public String description;

    public boolean isCompleted;
}

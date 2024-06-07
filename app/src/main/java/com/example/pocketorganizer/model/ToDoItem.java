package com.example.pocketorganizer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class ToDoItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "note_id")
    private int noteId;

    private String description;

    private boolean isCompleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete(){
        this.isCompleted = !this.isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}


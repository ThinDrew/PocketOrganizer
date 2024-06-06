package com.example.pocketorganizer.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class NoteWithToDo {
    @Embedded
    public Note note;

    @Relation(
            parentColumn = "id",
            entityColumn = "note_id"
    )
    public List<ToDoItem> toDoItems;
}

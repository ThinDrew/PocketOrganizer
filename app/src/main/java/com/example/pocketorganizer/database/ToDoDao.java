package com.example.pocketorganizer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pocketorganizer.model.ToDoItem;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert
    long insertToDoItem(ToDoItem toDoItem);

    @Update
    void updateToDoItem(ToDoItem toDoItem);

    @Delete
    void deleteToDoItem(ToDoItem toDoItem);

    @Query("SELECT * FROM todos WHERE note_id = :noteId")
    List<ToDoItem> getToDosForNoteId(int noteId);
    @Query("SELECT * FROM todos WHERE id = :id")
    ToDoItem getToDoForId(int id);

    @Query("SELECT * FROM todos")
    List<ToDoItem> getAllToDoItems();

    @Query("DELETE FROM todos")
    void deleteAllToDos();
}

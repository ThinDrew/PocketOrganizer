package com.example.pocketorganizer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pocketorganizer.model.Note;
import com.example.pocketorganizer.model.NoteWithToDo;

import java.util.List;

@Dao
public interface NoteDao {
    @Transaction
    @Query("SELECT * FROM notes WHERE id = :noteId")
    NoteWithToDo getNoteWithTodos(int noteId);

    @Insert
    long insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes WHERE date = :date")
    List<Note> getNotesForDate(String date);

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    Note getById(int noteId);

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}


package com.example.pocketorganizer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private String date;

    private boolean checked;

    private String notificationTime;

    // Getters и Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public void check() {
        this.checked = !this.checked;
    }

    public boolean isChecked(){
        return this.checked;
    }

    public String getNotificationTime() {
        return this.notificationTime;
    }

    public void setNotificationTime(String time) {
        this.notificationTime = time;
    }
}

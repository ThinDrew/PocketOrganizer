package com.example.pocketorganizer;

public class DailyNote {
    private String title;
    private String description;
    private boolean isDone;

    public DailyNote(String title, String description) {
        this.title = title;
        this.description = description;
        isDone = false;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.description = text;
    }

    public boolean isCompleted() {
        return isDone;
    }

    public void changeCompletion() {
        this.isDone = !isDone;
    }
}
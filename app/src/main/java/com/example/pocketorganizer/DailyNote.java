package com.example.pocketorganizer;

public class DailyNote {
    private String title;
    private String text;
    private boolean isDone;

    public DailyNote(String title, String text) {
        this.title = title;
        this.text = text;
        isDone = false;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return isDone;
    }

    public void changeCompletion() {
        this.isDone = !isDone;
    }
}
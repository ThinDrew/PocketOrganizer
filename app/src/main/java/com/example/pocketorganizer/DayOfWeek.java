package com.example.pocketorganizer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ArrayList;

public class DayOfWeek {
    private final String name;
    private LocalDate date;
    private ArrayList<DailyNote> noteList;

    public DayOfWeek(int number, int month, int year) {
        this.setDate(number, month, year);
        this.name = capitalize(this.date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")));
        noteList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase(new Locale("ru")) + str.substring(1);
    }

    public String getFormattedDate() {
        // Мы используем DateTimeFormatter для форматирования даты как "dd.MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    public ArrayList<DailyNote> getNotes() {
        return noteList;
    }

    public void setDate(int number, int month, int year) {
        try {
            this.date = LocalDate.of(year, month, number);
        } catch (Exception e) {
            // Если дата некорректная, устанавливаем текущую дату
            this.date = LocalDate.now();
        }
    }

    public boolean isCurrentDay() {
        return this.date.equals(LocalDate.now());
    }
}
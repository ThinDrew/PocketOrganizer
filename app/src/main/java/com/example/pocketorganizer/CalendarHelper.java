package com.example.pocketorganizer;

import com.example.pocketorganizer.enums.MonthName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarHelper {
    private Calendar calendar;
    private String monthText;
    public static final int weekSize = 7;
    public CalendarHelper(){
        this.calendar = Calendar.getInstance();
    }

    public List<DayOfWeek> getWeek(){
        // Устанавливаем день недели в понедельник
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Получаем название месяца и год
        this.monthText = MonthName.values()[this.calendar.get(Calendar.MONTH)].toString();
        this.monthText += " " + this.calendar.get(Calendar.YEAR);
        //Вывод текущей недели в список
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        for (int i = 0; i < weekSize; i++) {
            int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            int monthNumber = calendar.get(Calendar.MONTH) + 1;
            int yearNumber = calendar.get(Calendar.YEAR);
            DayOfWeek dayOfWeek = new DayOfWeek(dayNumber, monthNumber, yearNumber);
            daysOfWeek.add(dayOfWeek);
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Переходим к следующему дню
        }
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return daysOfWeek;
    }

    public int getWeekSize(){
        return weekSize;
    }

    public String getMonthText(){
        return this.monthText;
    }

    public void getPreviousWeek(){
        this.calendar.add(Calendar.DAY_OF_YEAR, -7);
    }

    public void getNextWeek(){
        this.calendar.add(Calendar.DAY_OF_YEAR, +7);
    }

    public void setWeekByDate(int day, int month, int year){
        this.calendar.set(year, month, day);
        // Помогает применить значения
        this.calendar.getTimeInMillis();
    }
}

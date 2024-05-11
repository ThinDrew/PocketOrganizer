package com.example.pocketorganizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarHelper {
    private Calendar calendar;
    private String monthText;
    public CalendarHelper(){
        this.calendar = Calendar.getInstance();
    }

    public List<DayOfWeek> getWeek(){
        Calendar currentCalendar = Calendar.getInstance();
        // Устанавливаем день недели в понедельник
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Получаем название месяца и год
        this.monthText = MonthName.values()[this.calendar.get(Calendar.MONTH)].toString();
        this.monthText += " " + this.calendar.get(Calendar.YEAR);
        //Вывод текущей недели в список
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int dayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            int monthNumber = calendar.get(Calendar.MONTH) + 1;
            DayOfWeek dayOfWeek = new DayOfWeek(DayOfWeekName.values()[i].toString(), dayNumber, monthNumber);
            //Проверка на то, является ли этот день текущим
            if (dayNumber == currentCalendar.get(Calendar.DAY_OF_MONTH) && (monthNumber == currentCalendar.get(Calendar.MONTH) + 1)){
                if(calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)){
                    dayOfWeek.setCurrentDay();
                }
            }
            daysOfWeek.add(dayOfWeek);
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Переходим к следующему дню
        }
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        this.calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return daysOfWeek;
    }

    public String getMonthText(){
        return this.monthText;
    }

    public List<DayOfWeek> getPreviousWeek(){
        this.calendar.add(Calendar.DAY_OF_YEAR, -7);
        return this.getWeek();
    }

    public List<DayOfWeek> getNextWeek(){
        this.calendar.add(Calendar.DAY_OF_YEAR, +7);
        return this.getWeek();
    }
}

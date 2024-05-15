package com.example.pocketorganizer;

import java.util.ArrayList;

public class DayOfWeek {
    private String name;
    private String numberAndMonth;
    private Integer number;
    private Integer month;
    private boolean isCurrent;
    private ArrayList<DailyNote> noteList;

    public DayOfWeek(String name, Integer number, Integer month) {
        this.setName(name);
        this.setNumberAndMonth(number, month);
        this.isCurrent = false;
        noteList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getNumberAndMonth() {
        return numberAndMonth;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<DailyNote> getNotes(){
        return noteList;
    }

    public void addNote(DailyNote note){
        noteList.add(note);
    }

    public void setNumberAndMonth(Integer number, Integer month){
        if(month > 12 || month < 1){
            this.month = 1;
        }
        else this.month = month;

        if(number > 31 || number < 1){
            this.number = 1;
        }
        else this.number = number;

        //Если число меньше 10, перед ним будет поставлен ноль
        this.numberAndMonth = this.number < 10? "0" + this.number + "." : this.number + ".";
        this.numberAndMonth += this.month < 10? "0" + this.month : this.month.toString();
    }

    public boolean isCurrentDay(){
        return isCurrent;
    }

    public void setCurrentDay(){
        this.isCurrent = true;
    }
}


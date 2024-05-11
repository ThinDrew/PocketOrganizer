package com.example.pocketorganizer;

import java.util.List;

public class DayOfWeek {
    private String name;
    private String numberAndMonth;
    private Integer number;
    private Integer month;
    private List<DailyNote> noteList;

    public DayOfWeek(String name, Integer number, Integer month) {
        this.setName(name);
        this.setNumberAndMonth(number, month);
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
        this.numberAndMonth = this.number < 10? "0" + this.number + "." : this.number.toString();
        this.numberAndMonth += this.month < 10? "0" + this.month : this.month.toString();
    }
}


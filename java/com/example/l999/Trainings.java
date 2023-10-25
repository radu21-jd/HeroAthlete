package com.example.l999;

public class Trainings {
    int year;
    int month;
    int dayOfMonth;
    int hour;
    int minute;
    Boolean completed;
    String id;
    String type;

    public Trainings(){
        //
    }

    public Trainings(int year, int month, int dayOfMonth, int hour, int minute, Boolean completed, String type){
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.hour=hour;
        this.minute=minute;
        this.completed=completed;
        this.type=type;
    }

    public int getYear(){return year;}
    public int getMonth(){return month;}
    public int getDayOfMonth(){return dayOfMonth;}
    public int getHour(){return hour;}
    public int getMinute(){return minute;}
    public Boolean getCompleted(){return completed;}
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType(){return type;}

}

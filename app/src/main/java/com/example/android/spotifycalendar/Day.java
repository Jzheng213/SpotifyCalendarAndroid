package com.example.android.spotifycalendar;

import java.util.Calendar;

public class Day {
    private int MonthNum;
    private Calendar date;
    private String[] Events;
    private int DayNumber;

    public Day(Calendar date, String[] events){
        this.date = date;
        Events = events;
    }

    public int getMonthNum() {
        return MonthNum;
    }

    public int getDayOfMonth() {
        return date.get(Calendar.DATE);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String[] getEvents() {
        return Events;
    }

    public void addEvent(String event) {
        //TODO: append to event list
    }

    public void setEvents(String[] events) {
        Events = events;
    }
}

package com.example.android.spotifycalendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Day {
    private int monthNum;
    private Calendar date;
    private ArrayList<Event> Events;
    private int dayNum;
    private int year;

    public Day(Calendar date, ArrayList<Event> events){
        this.date = date;
        monthNum = date.get(Calendar.MONTH);
        dayNum = date.get(Calendar.DAY_OF_MONTH);
        year = date.get(Calendar.YEAR);
        Events = events;
    }

    public int getDayOfMonth() {
        return date.get(Calendar.DATE);
    }
    public String getFormattedDate(){
        DateFormatSymbols dfs = new DateFormatSymbols();
        String day = dfs.getShortWeekdays()[date.get(Calendar.DAY_OF_WEEK)];
        String month = dfs.getShortMonths()[monthNum];

        return String.format("%s, %s %d, %d", day, month, dayNum, year);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public ArrayList<Event> getEvents() {
        if(Events != null){
            return Events;
        } else {
            return new ArrayList<Event>(){};
        }
    }

    public Calendar beginningOfDay() {
        return date;
    }

    public Calendar endOfDay(){
        Calendar eod = new GregorianCalendar(year, monthNum, dayNum);
        eod.add(Calendar.DAY_OF_MONTH, 1);
        eod.add(Calendar.MILLISECOND, -1);
        return eod;
    }
    public void addEvent(Event event) {
        //TODO: append to event list
    }
}

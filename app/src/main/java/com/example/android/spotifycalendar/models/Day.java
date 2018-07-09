package com.example.android.spotifycalendar.models;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Day {
    private Calendar date;
    private ArrayList<Event> Events;

    public Day(Calendar date, ArrayList<Event> events){
        this.date = date;
        Events = events;
    }

    public int getDayOfMonth() {
        return date.get(Calendar.DATE);
    }
    public String getFormattedDate(){
        DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
        return dayFormat.format(date.getTime());
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

    public void addEvent(Event event) {
        //TODO: append to event list
    }
}

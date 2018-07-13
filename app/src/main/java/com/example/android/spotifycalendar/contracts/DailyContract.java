package com.example.android.spotifycalendar.contracts;

import android.content.Context;

import com.example.android.spotifycalendar.models.Event;

import java.util.ArrayList;

public interface DailyContract {
    interface View{
        void refreshList();
        String getTargetDate();
    }

    interface Presenter{
        void deleteTask(Context context, int position);
        void setEvents(ArrayList<Event> events);
        ArrayList<Event> getEvents();
        Event getEventByPos(int position);
        void removeEventByPos(int position);
        void addEvent(Event event);
    }
}

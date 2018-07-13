package com.example.android.spotifycalendar.presenters;

import android.content.Context;

import com.example.android.spotifycalendar.contracts.DailyContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.APIEventUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DailyPresenter implements DailyContract.Presenter {
    ArrayList<Event> events;
    DailyContract.View mView;
    private DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

    public DailyPresenter(DailyContract.View view){
        mView = view;
    }

    public void deleteTask(Context context, int position) {
        APIEventUtil.deleteEvent(events.get(position).getID(), context);
        events.remove(position);
        mView.refreshList();
    }

    @Override
    public void setEvents(ArrayList<Event> events){
        this.events = events;
    }

    @Override
    public ArrayList<Event> getEvents(){
        return events;
    }

    @Override
    public Event getEventByPos(int position){
        return events.get(position);
    }

    @Override
    public void removeEventByPos(int position){
        events.remove(position);
    }

    @Override
    public void addEvent(Event event){
        if(dayFormat.format(event.getStartTime().getTime()).equals(mView.getTargetDate())){
            events.add(event);
        }
    }

}

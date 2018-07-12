package com.example.android.spotifycalendar.presenters;

import android.content.Context;

import com.example.android.spotifycalendar.contracts.DailyContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.APIEventUtil;

import java.util.ArrayList;

public class DailyPresenter implements DailyContract.Presenter {
    ArrayList<Event> events;
    DailyContract.View mView;

    public DailyPresenter(DailyContract.View view){
        mView = view;
    }

    public void deleteTask(Context context, int position) {
        APIEventUtil.deleteEvent(events.get(position).getID(), context);
        events.remove(position);
        mView.refreshList();
    }
}

package com.example.android.spotifycalendar.presenters;

import android.content.Context;

import com.example.android.spotifycalendar.contracts.MonthlyContract;
import com.example.android.spotifycalendar.models.Day;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.APIEventUtil;
import com.example.android.spotifycalendar.utils.CalendarHelper;
import com.example.android.spotifycalendar.utils.ResponseListener;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MonthlyPresenter implements MonthlyContract.Presenter {

    private MonthlyContract.View mView;

    private Calendar currentCalendar;
    private List<Day> days;
    private HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();

    public MonthlyPresenter(MonthlyContract.View view) {
        mView = view;
        currentCalendar = Calendar.getInstance();
    }

    @Override
    public void prevMonth(){
        currentCalendar.add(Calendar.MONTH, -1);
        refreshCalendar();
    }

    @Override
    public void nextMonth(){
        currentCalendar.add(Calendar.MONTH, 1);
        refreshCalendar();
    }

    public void refreshCalendar(){
        populateTitle();
        populateCalendar();
    }

    private void populateTitle(){
        int monthNum = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        mView.displayTitle(String.format("%s %d", months[monthNum], year));

    }

    private void populateCalendar(){
        days = new ArrayList<>();
        int daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int monthNum = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        int firstDay = new GregorianCalendar(year, monthNum, 1).get(Calendar.DAY_OF_WEEK);

        int day = 1;
        int maxCards = firstDay + daysInMonth > 36 ? 42 : 35;

        for (int i = 1; i <= maxCards; i++){
            if(i >= firstDay && i < firstDay + daysInMonth){
                GregorianCalendar currentDay = new GregorianCalendar(year, monthNum, day);
                String key = CalendarHelper.CalToKey(currentDay);
                days.add(new Day(currentDay, mappedEvents.get(key)));
                day++;
            }else{
                days.add(null);
            }
        }
        mView.setRecyclerView(days);
    }

    @Override
    public void getEvents(Context context){
        APIEventUtil.getEvents(context, new ResponseListener() {
            @Override
            public void onResponse(Object response) {
                mappedEvents = CalendarHelper.mapEvents((JSONObject) response);
                refreshCalendar();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

}


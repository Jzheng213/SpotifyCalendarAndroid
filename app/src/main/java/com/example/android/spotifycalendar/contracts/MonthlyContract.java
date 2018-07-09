package com.example.android.spotifycalendar.contracts;

import android.content.Context;

import com.example.android.spotifycalendar.models.Day;

import java.util.List;

public interface MonthlyContract {

    interface View {
        void displayTitle(String title);
        void setRecyclerView(List<Day> days);
    }

    interface Presenter {
        void getEvents(Context context);
        void refreshCalendar();
        void nextMonth();
        void prevMonth();
    }
}

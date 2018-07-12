package com.example.android.spotifycalendar.contracts;

import com.example.android.spotifycalendar.models.Event;

import java.util.Date;

public interface EventFormContract {

    int START_TIME = 0;
    int END_TIME = 1;

    interface View {
        void setTitle(String title);
        void setDescription(String description);
        void setTime(String time, int targetTime);

    }

    interface Presenter {
        void loadInitialTime(Date date);
        void loadEntries();
        int getCalInfo(int targetCal, int calendarInfo);
        void setCal(int targeCal, int year, int month, int day);
        void setCal(int targetCal, int calendarInfo, int input);
        void loadTime(int targetCal);
        Event loadEvent();
        void setEvent(Object event);
    }
}

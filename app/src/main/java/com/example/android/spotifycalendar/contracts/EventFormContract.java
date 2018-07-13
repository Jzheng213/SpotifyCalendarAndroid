package com.example.android.spotifycalendar.contracts;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.android.spotifycalendar.models.Event;

import org.json.JSONObject;

import java.util.Date;

public interface EventFormContract {

    int START_TIME = 0;
    int END_TIME = 1;

    int POST_MODE = 0;
    int PUT_MODE = 1;

    interface View {
        void setTitle(String title);
        void setDescription(String description);
        void setTime(String time, int targetTime);
        CharSequence getTitleText();
        CharSequence getDescriptionText();
        void showToast(String msg);
        void successfulResponse(int method, JSONObject jsonObject);
        void failedResponse(int method, String msg);
    }

    interface Presenter {
        void loadInitialTime();
        void loadEntries();
        int getCalInfo(int targetCal, int calendarInfo);
        void setCal(int targetCal, int year, int month, int day);
        void setCal(int targetCal, int calendarInfo, int input);
        void loadTime(int targetCal);
        Event loadEvent();
        void saveEvent(Context context, int mode);
        void setEvent(Object event);
        void setNewEvent(String date);
    }
}

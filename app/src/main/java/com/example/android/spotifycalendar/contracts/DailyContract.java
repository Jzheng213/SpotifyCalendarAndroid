package com.example.android.spotifycalendar.contracts;

import android.content.Context;

public interface DailyContract {
    interface View{
        void refreshList();
    }

    interface Presenter{
        void deleteTask(Context context, int position);
    }
}

package com.example.android.spotifycalendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class TestAPI {
    private static Calendar startTime = Calendar.getInstance();
    private static Calendar endTime = Calendar.getInstance();
    public static ArrayList<Event> seedEvents(){
        ArrayList<Event> events = new ArrayList<>();
        startTime.set(2018, 6, 3);
        endTime.set(2018, 6, 3);

        modifyTime(startTime, 8, 15);
        modifyTime(endTime, 9, 15);
        events.add(new Event("Do Laundry", "can't have dirty laundry", startTime, endTime));

        modifyTime(startTime, 10, 15);
        modifyTime(endTime, 13, 15);
        events.add(new Event("Shopping", "because gots money", startTime, endTime));

        modifyTime(startTime, 12, 13);
        modifyTime(endTime, 23, 15);
        events.add(new Event("Walk Dog", "Lassy will poop in the house", startTime, endTime));

        modifyTime(startTime, 14, 15);
        modifyTime(endTime, 15, 40);
        events.add(new Event("Call Insurance", "reduce insurance cost", startTime, endTime));

        return events;
    }

    private static void modifyTime(Calendar cal, int hour, int min){
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
    }
}

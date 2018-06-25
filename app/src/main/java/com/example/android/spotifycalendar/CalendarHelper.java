package com.example.android.spotifycalendar;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarHelper {

    public static HashMap<String, ArrayList<Event>> mapEvents(){
        HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();
        ArrayList<Event> events = TestAPI.seedEvents();

        for(Event event: events){
            Calendar startTime = event.getStartTime();
            String key = CalToKey(startTime);
            if (mappedEvents.containsKey(key)) {
                mappedEvents.get(key).add(event);
            }else{
                mappedEvents.put(key,new ArrayList<Event>(Arrays.asList(event)));
            }
        }

        return mappedEvents;
    };

    public static HashMap<String, ArrayList<Event>> mapEvents(JSONObject json){
        HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();
        JSONObject json2;
        return mappedEvents;
    };

    public static String CalToKey(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);

        return String.format("%d%d%d",year,month,date);

    }
}

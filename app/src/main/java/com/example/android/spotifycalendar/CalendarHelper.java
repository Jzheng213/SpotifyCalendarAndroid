package com.example.android.spotifycalendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static HashMap<String, ArrayList<Event>> mapEvents(JsonObject json){
        HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();
        JsonArray events = json.getAsJsonArray("events");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        for (JsonElement jsonEl : events) {
            JsonObject event = jsonEl.getAsJsonObject();
            Integer id = event.get("id").getAsInt();
            String title = event.get("title").getAsString();
            String description = event.get("description").getAsString();
            try {
                startTime.setTime(sdf.parse(event.get("start_time").getAsString()));
                endTime.setTime(sdf.parse(event.get("end_time").getAsString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Event eventObj = new Event(title, description, (Calendar) startTime.clone(), (Calendar) endTime.clone());
            String key = CalToKey(startTime);
            if (mappedEvents.containsKey(key)) {
                mappedEvents.get(key).add(eventObj);
            }else{
                mappedEvents.put(key,new ArrayList<Event>(Arrays.asList(eventObj)));
            }
        }

        return mappedEvents;
    };

    public static String CalToKey(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DAY_OF_MONTH);

        return String.format("%d%d%d",year,month,date);

    }
}

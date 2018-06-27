package com.example.android.spotifycalendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarHelper {

    public static HashMap<String, ArrayList<Event>> mapEvents(JSONObject json) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();
        try{
            JSONArray events = json.getJSONArray("events");
            for(int i = 0; i < events.length(); i++){
                JSONObject event = events.getJSONObject(i);
                int id = event.getInt("id");
                String title = event.getString("title");
                String description = event.getString("description");
                startTime.setTime(sdf.parse(event.getString("start_time")));
                endTime.setTime(sdf.parse(event.getString("end_time")));

                Event eventObj = new Event(title, description, (Calendar) startTime.clone(), (Calendar) endTime.clone());

                String key = CalToKey(startTime);
                if (mappedEvents.containsKey(key)) {
                    mappedEvents.get(key).add(eventObj);
                }else{
                    mappedEvents.put(key,new ArrayList<Event>(Arrays.asList(eventObj)));
                }
            }
        } catch(JSONException jsone){
            // throw exception
        } catch(ParseException pe){
            // throw exception
        }

        return mappedEvents;
    }

    public static String CalToKey(Calendar cal){
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MMM-dd");
        return dfs.format(cal.getTime());
    }
}

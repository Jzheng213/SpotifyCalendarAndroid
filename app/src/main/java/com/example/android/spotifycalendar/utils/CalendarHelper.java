package com.example.android.spotifycalendar.utils;

import android.util.Log;

import com.example.android.spotifycalendar.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarHelper {

    public static HashMap<String, ArrayList<Event>> mapEvents(JSONObject json) {
        HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();
        try{
            JSONArray events = json.getJSONArray("events");
            for(int i = 0; i < events.length(); i++){
                JSONObject event = events.getJSONObject(i);
                Event eventObj = JSONHelper.createEventObject(event);

                String key = CalToKey(eventObj.getStartTime());
                if (mappedEvents.containsKey(key)) {
                    mappedEvents.get(key).add(eventObj);
                }else{
                    mappedEvents.put(key,new ArrayList<Event>(Arrays.asList(eventObj)));
                }
            }
        } catch(JSONException jsone){
            Log.e("downloading_events", "failed to download events" + jsone);
        }

        return mappedEvents;
    }

    public static String CalToKey(Calendar cal){
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MMM-dd");
        return dfs.format(cal.getTime());
    }
}

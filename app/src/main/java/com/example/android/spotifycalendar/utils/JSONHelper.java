package com.example.android.spotifycalendar.utils;

import android.util.Log;

import com.example.android.spotifycalendar.models.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JSONHelper {
    public static Event createEventObject(JSONObject event){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try{
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();

            int id = event.getInt("id");
            String title = event.getString("title");
            String description = event.getString("description");
            startTime.setTime(sdf.parse(event.getString("start_time")));
            endTime.setTime(sdf.parse(event.getString("end_time")));

            Event eventObj = new Event(id, title, description, (Calendar) startTime.clone(), (Calendar) endTime.clone());

            return eventObj;

        } catch(JSONException jsone){
            Log.e("build_event_obj", "event object failed to build" + jsone);
            return null;
        } catch(ParseException pe){
            Log.e("build_event_obj", "Event object failed to build" + pe);
            return null;
        }
    }
}

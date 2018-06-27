package com.example.android.spotifycalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.spotifycalendar.models.Day;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.CalendarHelper;
import com.example.android.spotifycalendar.utils.VolleySingleton;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MonthlyActivity extends AppCompatActivity {

    private JsonObjectRequest jsonObjectRequest;

    private Calendar currentCalendar;
    private List<Day> listDay;

    private HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        currentCalendar = Calendar.getInstance();
        getEvents();
        refreshCalendar();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getEvents();
        refreshCalendar();
    }

    public void prev_month(View view){
        currentCalendar.add(Calendar.MONTH, -1);
        refreshCalendar();
    }

    public void next_month(View view){
        currentCalendar.add(Calendar.MONTH, 1);
        refreshCalendar();
    }

    private void refreshCalendar(){
        populateTitle();
        populateCalendar();
    }

    private void populateTitle(){
        int monthNum = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);

        TextView monthTitle = (TextView) findViewById(R.id.month_id);
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        monthTitle.setText(String.format("%s %d", months[monthNum], year));
    }

    private void populateCalendar(){
        listDay = new ArrayList<>();
        int daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int monthNum = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        int firstDay = new GregorianCalendar(year, monthNum, 1).get(Calendar.DAY_OF_WEEK);

        int day = 1;
        int maxCards = firstDay + daysInMonth > 36 ? 42 : 35;

        for (int i = 1; i <= maxCards; i++){
            if(i >= firstDay && i < firstDay + daysInMonth){
                GregorianCalendar currentDay = new GregorianCalendar(year, monthNum, day);
                String key = CalendarHelper.CalToKey(currentDay);
                listDay.add(new Day(currentDay, mappedEvents.get(key)));
                day++;
            }else{
                listDay.add(null);
            }
        }

        RecyclerView month = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter monthlyAdapter = new RecyclerViewAdapter(this, listDay);

        month.setLayoutManager(new GridLayoutManager(this, 7));
        month.setAdapter(monthlyAdapter);
    }

    public void getEvents(){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";

        jsonObjectRequest = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response){
                    JSONObject json = response;
                    mappedEvents = CalendarHelper.mapEvents(json);
                    refreshCalendar();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                Log.e("Volley","Error");
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}

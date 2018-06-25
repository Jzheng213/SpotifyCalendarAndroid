package com.example.android.spotifycalendar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MonthlyActivity extends AppCompatActivity {
    private Calendar currentCalendar;
    private List<Day> listDay;

    private HashMap<String, ArrayList<Event>> mappedEvents = new HashMap<>();

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
//        mappedEvents = CalendarHelper.mapEvents();

        currentCalendar = Calendar.getInstance();
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
        Ion.with(this)
                .load("https://spotify-calendar-backend.herokuapp.com/api/events")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                     @Override
                     public void onCompleted(Exception e, JsonObject result) {
                     if (result != null){
                             JsonObject json = result;
                             mappedEvents = CalendarHelper.mapEvents(json);
                             refreshCalendar();
                     } else {
                         Toast.makeText(MonthlyActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }}
                }
        );
    }
}

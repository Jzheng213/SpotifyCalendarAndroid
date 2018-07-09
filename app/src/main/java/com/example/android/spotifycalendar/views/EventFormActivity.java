package com.example.android.spotifycalendar.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.JSONHelper;
import com.example.android.spotifycalendar.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private int mode;
    final public static int POST_MODE = 0;
    final public static int PATCH_MODE = 1;
    private int id, position;
    private TextView title, description;
    private String currentDateString;
    private Calendar startCal, endCal;
    private Event event;
    TextView startTime, endTime;
    private boolean startSelected;
    private DateFormat dfs = new SimpleDateFormat("EEE, MMM, dd yyyy hh:mm a");
    private DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        if (mode == PATCH_MODE) event = intent.getParcelableExtra("event");

        title = findViewById(R.id.post_title_id);
        description = findViewById(R.id.post_description_id);
        startTime = findViewById(R.id.start_time_id);
        endTime = findViewById(R.id.end_time_id);

        if(mode == POST_MODE){
            currentDateString = intent.getStringExtra("currentDate");
            Date currentDate;
            try{
                currentDate = dayFormat.parse(currentDateString);
            } catch(ParseException pe){
                Log.e("parsing_current_date", "failed parsing: " + pe.getMessage());
                currentDate = Calendar.getInstance().getTime();
            }

            setInitialTime(currentDate);
        } else {
            position = intent.getIntExtra("position", -1);
            setEntries();
        }

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(startCal);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(endCal);
            }
        });


    }

    private void setInitialTime(Date currentDate){

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        int currentHour = startCal.get(Calendar.HOUR_OF_DAY);
        if (startCal.get(Calendar.MINUTE) != 0) currentHour++;

        startCal.setTime(currentDate);
        endCal.setTime(currentDate);

        startCal.add(Calendar.HOUR, currentHour);
        endCal.add(Calendar.HOUR, currentHour + 1);

        startTime.setText(dfs.format(startCal.getTime()));
        endTime.setText(dfs.format(endCal.getTime()));
    }

    private void setEntries(){
        id = event.getID();
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        startCal = event.getStartTime();
        endCal = event.getEndTime();

        startTime.setText(dfs.format(startCal.getTime()));
        endTime.setText(dfs.format(endCal.getTime()));
    }

    private void beforeDateSet(Calendar cal){

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        this.startSelected = startCal == cal;

        DatePickerDialog datePickerDialog = new DatePickerDialog(EventFormActivity.this, EventFormActivity.this, year, month, day);
        datePickerDialog.show();
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day){
        Calendar targetCal;

        if (startSelected) {
            targetCal = startCal;
        } else {
            targetCal = endCal;
        };

        targetCal.set(year, month, day);

        int hour = targetCal.get(Calendar.HOUR_OF_DAY);
        int minute = targetCal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog( EventFormActivity.this, EventFormActivity.this, hour, minute, false);
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker timePicker, int hour, int min){

        TextView targetView;
        Calendar targetCal;
        dfs.setTimeZone(startCal.getTimeZone());

        if(startSelected){
            targetCal = startCal;
            targetView = (TextView) findViewById(R.id.start_time_id);
        } else {
            targetCal = endCal;
            targetView = (TextView) findViewById(R.id.end_time_id);
        }

        targetCal.set(Calendar.HOUR_OF_DAY, hour);
        targetCal.set(Calendar.MINUTE, min);
        targetView.setText(dfs.format(targetCal.getTime()));
    }

    private JSONObject eventJsonBuilder(){
        TextView title = findViewById(R.id.post_title_id);
        TextView description = findViewById(R.id.post_description_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        JSONObject json = new JSONObject();
        JSONObject event = new JSONObject();
        try {
            event.put("title", title.getText());
            event.put("description", description.getText());
            event.put("start_time", sdf.format(startCal.getTime()));
            event.put("end_time", sdf.format(endCal.getTime()));
            event.put("all_day", false);
            json.put("event", event);
        } catch (JSONException jsone){
            Log.e("json_builder", "failed build");
        }

        return json;
    }


    //TODO: move Validations to Event class
    private boolean validateAll(){
        return  validateNotEmptyString("Title", title) &&
                validateEndDateAfterStartDate() &&
                validateNotSameDay();
    }

    private boolean validateNotEmptyString(String title, TextView target){
        if( target.getText().length() == 0){
            Toast.makeText(this, String.format( "%s can't be blank", title), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateEndDateAfterStartDate(){
        if( startCal.compareTo(endCal) != -1){
            Toast.makeText(this, "Your start time can't be greater than your end time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateNotSameDay(){
        if (!dayFormat.format(startCal.getTime()).equals(dayFormat.format(endCal.getTime()))) {
            Toast.makeText(this, "Events can't span across multiple days", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean sendEvent(int method, String url){

        if(!validateAll()) return false;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method,
                url,
                eventJsonBuilder(),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String toastResponse = (mode == POST_MODE) ? "event added" : "event updated";
                Log.e("post_request", "successful request");
                Toast.makeText(EventFormActivity.this, toastResponse, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                try{
                    Event event = JSONHelper.createEventObject(response.getJSONObject("event"));
                    resultIntent.putExtra("event", event);
                    if(position != -1) resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);

                    finish();
                } catch(JSONException jsone){
                    Log.e("post_request", "no events object returned from db after post request");
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("post_request", "failed request");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return true;
    }


    @Override
    public void onBackPressed(){
        cancelSubmit();
    }

    public void cancel(View view) {
        cancelSubmit();
    }

    private void cancelSubmit(){
        setResult(RESULT_CANCELED);
        finish();
    }
    public void submit(View view){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";
        switch (mode){
            case POST_MODE:
                sendEvent(Request.Method.POST, url);
                break;
            case PATCH_MODE:
                url = String.format(Locale.US, "%s/%d", url, id);
                sendEvent(Request.Method.PATCH, url);
                break;
        }
    }

}

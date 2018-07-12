package com.example.android.spotifycalendar.presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.contracts.EventFormContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.JSONHelper;
import com.example.android.spotifycalendar.utils.VolleySingleton;
import com.example.android.spotifycalendar.views.EventForm.EventFormActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.spotifycalendar.contracts.EventFormContract.END_TIME;
import static com.example.android.spotifycalendar.contracts.EventFormContract.START_TIME;

public class EventFormPresenter implements  EventFormContract.Presenter{

    private Calendar startCal, endCal;
    private DateFormat dfs = new SimpleDateFormat("EEE, MMM, dd yyyy hh:mm a");
    private Event event;

    private EventFormContract.View mView;

    public EventFormPresenter(EventFormContract.View view){
        mView = view;
        dfs.setTimeZone(startCal.getTimeZone());
    }


    @Override
    public void setEvent(Object event) {
        if(event instanceof Event) this.event = (Event) event;
    }

    @Override
    public Event loadEvent() {
        return event;
    }

    public void loadInitialTime(Date currentDate){

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        int currentHour = startCal.get(Calendar.HOUR_OF_DAY);
        if (startCal.get(Calendar.MINUTE) != 0) currentHour++;

        startCal.setTime(currentDate);
        endCal.setTime(currentDate);

        startCal.add(Calendar.HOUR, currentHour);
        endCal.add(Calendar.HOUR, currentHour + 1);

        mView.setTime(dfs.format(startCal.getTime()), START_TIME);
        mView.setTime(dfs.format(endCal.getTime()), END_TIME);
    }

    @Override
    public void setTitle(){

    }

    @Override
    public void loadEntries(){
        mView.setTitle(event.getTitle());
        mView.setDescription(event.getDescription());


        startCal = event.getStartTime();
        endCal = event.getEndTime();

        loadTime(START_TIME);
        loadTime(END_TIME);
    }

    @Override
    public void loadTime(int targetTime){
        mView.setTime(dfs.format(startCal.getTime()), targetTime);
    }

    @Override
    public int getCalInfo(int targetCal, int calendarMetric){
        switch (targetCal){
            case START_TIME:
                return startCal.get(calendarMetric);
            case END_TIME:
                return endCal.get(calendarMetric);
            default:
                return 0;
        }
    }

    @Override
    public void setCal(int targetCal, int year, int  month, int day){
        switch (targetCal){
            case START_TIME:
                startCal.set(year, month, day);
                break;
            case END_TIME:
                endCal.set(year, month, day);
                break;
        }
    }

    @Override
    public void setCal(int targetCal, int calendarMetric, int input) {
        switch (targetCal){
            case START_TIME:
                startCal.set(calendarMetric, input);
                break;
            case END_TIME:
                endCal.set(calendarMetric, input);
                break;
        }
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

    //TODO: move validations to presentation
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

    //TODO: move to APIEvents
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
}

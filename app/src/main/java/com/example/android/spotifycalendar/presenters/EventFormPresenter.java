package com.example.android.spotifycalendar.presenters;

import android.content.Context;
import android.util.Log;

import com.example.android.spotifycalendar.contracts.EventFormContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.APIEventUtil;
import com.example.android.spotifycalendar.utils.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.android.spotifycalendar.contracts.EventFormContract.END_TIME;
import static com.example.android.spotifycalendar.contracts.EventFormContract.POST_MODE;
import static com.example.android.spotifycalendar.contracts.EventFormContract.PUT_MODE;
import static com.example.android.spotifycalendar.contracts.EventFormContract.START_TIME;

public class EventFormPresenter implements  EventFormContract.Presenter{

    private Calendar startCal, endCal;
    private DateFormat dfs = new SimpleDateFormat("EEE, MMM, dd yyyy hh:mm a");
    private DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

    private Event event;

    private EventFormContract.View mView;

    public EventFormPresenter(EventFormContract.View view){
        mView = view;
    }

    @Override
    public void setEvent(Object event) {
        if(event instanceof Event) this.event = (Event) event;
    }

    @Override
    public Event loadEvent() {
        return event;
    }

    @Override
    public void setNewEvent(String date){

        Date currentDate;
        try{
            currentDate = dayFormat.parse(date);
        } catch(ParseException pe){
            Log.e("parsing_current_date", "failed parsing: " + pe.getMessage());
            currentDate = Calendar.getInstance().getTime();
        }

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        dfs.setTimeZone(startCal.getTimeZone());

        int currentHour = startCal.get(Calendar.HOUR_OF_DAY);
        if (startCal.get(Calendar.MINUTE) != 0) currentHour++;

        startCal.setTime(currentDate);
        endCal.setTime(currentDate);

        startCal.add(Calendar.HOUR, currentHour);
        endCal.add(Calendar.HOUR, currentHour + 1);

        this.event = new Event("", "", startCal,endCal);
    }

    public void loadInitialTime(){
        mView.setTime(dfs.format(startCal.getTime()), START_TIME);
        mView.setTime(dfs.format(endCal.getTime()), END_TIME);
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
        switch (targetTime){
            case START_TIME:
                mView.setTime(dfs.format(startCal.getTime()), START_TIME);
                break;
            case END_TIME:
                mView.setTime(dfs.format(endCal.getTime()), END_TIME);
                break;
        }
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        JSONObject json = new JSONObject();
        JSONObject event = new JSONObject();

        try {
            event.put("title", mView.getTitleText());
            event.put("description", mView.getDescriptionText());
            event.put("start_time", sdf.format(startCal.getTime()));
            event.put("end_time", sdf.format(endCal.getTime()));
            event.put("all_day", false);
            json.put("event", event);
        } catch (JSONException jsone){
            Log.e("json_builder", "failed build");
        }

        return json;
    }

    private boolean validateAll(){
        return  validateNotEmptyString("Title", mView.getTitleText()) &&
                validateEndDateAfterStartDate() &&
                validateNotSameDay();
    }

    private boolean validateNotEmptyString(String title, CharSequence text){
        if( text.length() == 0){
            mView.showToast(String.format( "%s can't be blank", title));
            return false;
        }
        return true;
    }

    private boolean validateEndDateAfterStartDate(){
        if( startCal.compareTo(endCal) != -1){
            mView.showToast("Your start time can't be greater than your end time");
            return false;
        }
        return true;
    }

    private boolean validateNotSameDay(){
        if (!dayFormat.format(startCal.getTime()).equals(dayFormat.format(endCal.getTime()))) {
            mView.showToast("Events can't span across multiple days");
            return false;
        }
        return true;
    }

    @Override
    public void saveEvent(Context context, int mode){
        if (validateAll()) {
            switch (mode) {
                case POST_MODE:
                    APIEventUtil.postEvent(context, eventJsonBuilder(), new ResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            JSONObject jsonResponse = (JSONObject) response;
                            mView.successfulResponse(POST_MODE, jsonResponse);
                        }

                        @Override
                        public void onError(String message) {
                            Log.e("post_request", "failed request");
                            mView.failedResponse(POST_MODE, message);
                        }
                    });
                    break;
                case PUT_MODE:
                    APIEventUtil.putEvent(context, eventJsonBuilder(), event.getID(), new ResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            JSONObject jsonResponse = (JSONObject) response;
                            Log.e("put_request", "successful request");
                            mView.showToast("event updated");
                            mView.successfulResponse(PUT_MODE, jsonResponse);
                        }

                        @Override
                        public void onError(String message) {
                            Log.e("put_request", "failed request");
                            mView.showToast("event updated");
                            mView.failedResponse(PUT_MODE, message);
                        }
                    });
                    break;
            }
        }
    }

}

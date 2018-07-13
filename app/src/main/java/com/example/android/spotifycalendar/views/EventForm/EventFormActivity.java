package com.example.android.spotifycalendar.views.EventForm;

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

import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.contracts.EventFormContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.presenters.EventFormPresenter;
import com.example.android.spotifycalendar.utils.JSONHelper;
import com.koushikdutta.async.http.body.JSONObjectBody;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.spotifycalendar.contracts.EventFormContract.END_TIME;
import static com.example.android.spotifycalendar.contracts.EventFormContract.START_TIME;
import static com.example.android.spotifycalendar.contracts.EventFormContract.POST_MODE;
import static com.example.android.spotifycalendar.contracts.EventFormContract.PUT_MODE;

import java.util.Calendar;

public class EventFormActivity
        extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        EventFormContract.View{

    private int mode;

    EventFormContract.Presenter mPresenter;
    private int position;
    private TextView title, description;
    TextView startTime, endTime;
    private int selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mPresenter = new EventFormPresenter(this);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        initViews();

        if (mode == PUT_MODE) mPresenter.setEvent(intent.getParcelableExtra("event"));


        if(mode == POST_MODE){
            mPresenter.setNewEvent(intent.getStringExtra("currentDate"));
            mPresenter.loadInitialTime();
        } else {
            position = intent.getIntExtra("position", -1);
            mPresenter.loadEntries();
        }

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(START_TIME);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(END_TIME);
            }
        });
    }

    private void initViews(){
        title = findViewById(R.id.post_title_id);
        description = findViewById(R.id.post_description_id);
        startTime = findViewById(R.id.start_time_id);
        endTime = findViewById(R.id.end_time_id);

    }

    @Override
    public void setTitle(String title){
        this.title.setText(title);
    };

    @Override
    public CharSequence getTitleText(){
        return this.title.getText();
    }

    @Override
    public void setDescription(String description){
        this.description.setText(description);
    }

    @Override
    public CharSequence getDescriptionText(){
        return this.description.getText();
    }
    @Override
    public void setTime(String time, int targetTime) {
        switch (targetTime) {
            case START_TIME:
                startTime.setText(time);
                break;
            case END_TIME:
                endTime.setText(time);
                break;
        }
    }

    @Override
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void beforeDateSet(int cal){
        int year = mPresenter.getCalInfo(cal, Calendar.YEAR);
        int month = mPresenter.getCalInfo(cal, Calendar.MONTH);
        int day = mPresenter.getCalInfo(cal, Calendar.DAY_OF_MONTH);

        this.selectedTime = cal;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EventFormActivity.this,
                EventFormActivity.this,
                year,
                month,
                day);
        datePickerDialog.show();
    }


    public void onDateSet(DatePicker datePicker, int year, int month, int day){
        mPresenter.setCal(selectedTime, year, month, day);

        int hour = mPresenter.getCalInfo(selectedTime, Calendar.HOUR_OF_DAY);
        int minute = mPresenter.getCalInfo(selectedTime, Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                EventFormActivity.this,
                EventFormActivity.this,
                hour,
                minute,
                false);

        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker timePicker, int hour, int min){
        mPresenter.setCal(selectedTime, Calendar.HOUR_OF_DAY, hour);
        mPresenter.setCal(selectedTime, Calendar.MINUTE, min);
        mPresenter.loadTime(selectedTime);
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
    //TODO: Create an onclick switch to trigger methods

    public void submit(View view){
        mPresenter.saveEvent(this, mode);
    }

    @Override
    public void successfulResponse(int method, JSONObject response){
        Intent resultIntent = new Intent();
        try {
            Event event = JSONHelper.createEventObject(response.getJSONObject("event"));
            resultIntent.putExtra("event", event);
            if (position != -1) resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (JSONException jsone) {
            Log.e("post_request", "no events object returned from db after post request");
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void failedResponse(int method, String msg){
        String toastMessage = String.format("upload failed: %s", msg);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }
}

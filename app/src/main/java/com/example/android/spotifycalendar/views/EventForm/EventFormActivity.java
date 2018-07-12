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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.contracts.EventFormContract;
import com.example.android.spotifycalendar.presenters.EventFormPresenter;
import com.example.android.spotifycalendar.utils.JSONHelper;
import com.example.android.spotifycalendar.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.spotifycalendar.contracts.EventFormContract.END_TIME;
import static com.example.android.spotifycalendar.contracts.EventFormContract.START_TIME;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventFormActivity
        extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        EventFormContract.View{

    private int mode;
    final public static int POST_MODE = 0;
    final public static int PATCH_MODE = 1;

    EventFormContract.Presenter mPresenter;
    private int position;
    private TextView title, description;
    private String currentDateString;
    TextView startTime, endTime;
    private int selectedTime;
    private DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mPresenter = new EventFormPresenter(this);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        initViews();

        if (mode == PATCH_MODE) mPresenter.setEvent(intent.getParcelableExtra("event"));


        if(mode == POST_MODE){
            currentDateString = intent.getStringExtra("currentDate");
            Date currentDate;
            try{
                currentDate = dayFormat.parse(currentDateString);
            } catch(ParseException pe){
                Log.e("parsing_current_date", "failed parsing: " + pe.getMessage());
                currentDate = Calendar.getInstance().getTime();
            }

            mPresenter.loadInitialTime(currentDate);
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
    public void setDescription(String description){
        this.description.setText(description);
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

    private void beforeDateSet(int cal){

        int year = mPresenter.getCalInfo(cal, Calendar.YEAR);
        int month = mPresenter.getCalInfo(cal, Calendar.MONTH);
        int day = mPresenter.getCalInfo(cal, Calendar.DAY_OF_MONTH);

        this.selectedTime = START_TIME;

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
    
    //TODO: move to presentation 
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

package com.example.android.spotifycalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Calendar startCal, endCal;
    private boolean startSelected;
    private DateFormat dfs = new SimpleDateFormat("EEE, MMM, dd yyyy hh:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView startTime = (TextView) findViewById(R.id.start_time_id);
        TextView endTime = (TextView) findViewById(R.id.end_time_id);

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        endCal.add(Calendar.HOUR, 1);
        startTime.setText(dfs.format(startCal.getTime()));
        endTime.setText(dfs.format(endCal.getTime()));

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(true);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeDateSet(false);
            }
        });
    }

    public void beforeDateSet(boolean startSelected){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        this.startSelected = startSelected;

        DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, PostActivity.this, year, month, day);
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

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog( PostActivity.this, PostActivity.this, hour, minute, false);
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

        targetCal.set(Calendar.HOUR, hour);
        targetCal.set(Calendar.MINUTE, min);
        targetView.setText(dfs.format(targetCal.getTime()));
    }


}

package com.example.android.spotifycalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthlyActivity extends AppCompatActivity {
    private Calendar currentCalendar;
    List<Day> listDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        currentCalendar = Calendar.getInstance();
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
        int maxDays = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int monthNum = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        for (int i = 1; i <= maxDays; i++){
            listDay.add(new Day(new GregorianCalendar(year, monthNum, i), new String[]{"" + i + " event", "do Something else"}));
        }

        RecyclerView month = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter monthlyAdapter = new RecyclerViewAdapter(this, listDay);

        month.setLayoutManager(new GridLayoutManager(this, 7));
        month.setAdapter(monthlyAdapter);
    }
}

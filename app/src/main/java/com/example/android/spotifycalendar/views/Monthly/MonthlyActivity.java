package com.example.android.spotifycalendar.views.Monthly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.contracts.MonthlyContract;
import com.example.android.spotifycalendar.models.Day;
import com.example.android.spotifycalendar.presenters.MonthlyPresenter;

import java.util.List;


public class MonthlyActivity extends AppCompatActivity
        implements MonthlyContract.View, View.OnClickListener{

    TextView mMonthTitle;

    private MonthlyContract.Presenter mPresenter;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        initViews();

        mPresenter = new MonthlyPresenter(this);
        mPresenter.getEvents(this);
        mPresenter.refreshCalendar();
    }

    private void initViews() {
        mMonthTitle = findViewById(R.id.month_id);
        findViewById(R.id.next_month_id).setOnClickListener(this);
        findViewById(R.id.prev_month_id).setOnClickListener(this);
    }

    @Override
    public void displayTitle(String title) {
        mMonthTitle.setText(title);
    }

    @Override
    public void setRecyclerView(List<Day> days){
        RecyclerView month = findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter monthlyAdapter = new RecyclerViewAdapter(this, days);

        month.setLayoutManager(new GridLayoutManager(this, 7));
        month.setAdapter(monthlyAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPresenter.getEvents(this);
        mPresenter.refreshCalendar();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.prev_month_id:
                mPresenter.prevMonth();
                mPresenter.getEvents(this);
                mPresenter.refreshCalendar();
                break;
            case R.id.next_month_id:
                mPresenter.nextMonth();
                mPresenter.getEvents(this);
                mPresenter.refreshCalendar();
                break;
        }
    }

}

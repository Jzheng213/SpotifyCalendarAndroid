package com.example.android.spotifycalendar.views.Daily;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.contracts.DailyContract;
import com.example.android.spotifycalendar.contracts.EventFormContract;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.presenters.DailyPresenter;
import com.example.android.spotifycalendar.views.EventForm.EventFormActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DayActivity extends AppCompatActivity
        implements DailyContract.View{

    private DailyContract.Presenter mPresenter;

    private DateFormat dfs = new SimpleDateFormat("EEE, MMM, dd yyyy hh:mm a");
    private String targetDate;
    private TextView tvDate;

    final private int POST_EVENT_REQUEST_CODE = 1;
    final private int PATCH_EVENT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        mPresenter = new DailyPresenter(this);
        initViews();
        refreshList();
    }
    
    private void initViews(){
        Intent intent = getIntent();
        targetDate = intent.getStringExtra("date");
        if (intent.getExtras() != null){
            mPresenter.setEvents((ArrayList<Event>) intent.getExtras().getSerializable("events"));
        }
        
        tvDate = findViewById(R.id.daycard_date_id);
        tvDate.setText(targetDate);
    }

    @Override
    public void refreshList(){
        ListView eventListView = findViewById(R.id.daycard_events_id);
        EventsAdapter eventsAdapter = new EventsAdapter(this, mPresenter.getEvents());
        eventListView.setAdapter(eventsAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                Event event = mPresenter.getEventByPos(position);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(DayActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_show_task, null);
                TextView title = mView.findViewById(R.id.show_title_id);
                TextView description = mView.findViewById(R.id.show_description_id);
                TextView start_time = mView.findViewById(R.id.show_start_time_id);
                TextView end_time = mView.findViewById(R.id.show_end_time_id);

                title.setText(event.getTitle());
                description.setText(event.getDescription());
                start_time.setText(String.format("Start: %s", dfs.format(event.getStartTime().getTime())));
                end_time.setText(String.format("End: %s", dfs.format(event.getEndTime().getTime())));

                Button edit = mView.findViewById(R.id.show_edit_button_id);
                Button delete = mView.findViewById(R.id.show_delete_button_id);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                edit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        updateTask(position);
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        deleteTask(position);
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void deleteTask(int position){
        mPresenter.deleteTask(this, position);
    }

    public void updateTask (int position) {
        Intent intent = new Intent(this, EventFormActivity.class);
        intent.putExtra("mode", EventFormContract.PUT_MODE);
        intent.putExtra("event", mPresenter.getEventByPos(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, PATCH_EVENT_REQUEST_CODE);
    }

    public void createNewTask (View view) {
        Intent intent = new Intent(this, EventFormActivity.class);
        intent.putExtra("mode", EventFormContract.POST_MODE);
        intent.putExtra("currentDate", targetDate);
        startActivityForResult(intent, POST_EVENT_REQUEST_CODE);
    }

    @Override
    public String getTargetDate(){
        return targetDate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == POST_EVENT_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Event event = data.getParcelableExtra("event");
                mPresenter.addEvent(event);
                refreshList();
            }
        }

        if(requestCode == PATCH_EVENT_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Event event = data.getParcelableExtra("event");
                int position = data.getIntExtra("position", -1);
                if(position != -1){
                    mPresenter.removeEventByPos(position);
                    mPresenter.addEvent(event);
                }
                refreshList();
            }
        }
    }


}

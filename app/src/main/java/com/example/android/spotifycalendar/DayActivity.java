package com.example.android.spotifycalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.spotifycalendar.models.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DayActivity extends AppCompatActivity {

    private String TargetDate;
    private ArrayList<Event> Events;
    private TextView tvDate;
    final private int POST_EVENT_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);


        Intent intent = getIntent();
        TargetDate = intent.getStringExtra("date");
        Events = (ArrayList<Event>) intent.getExtras().getSerializable("events");

        tvDate = findViewById(R.id.daycard_date_id);
        tvDate.setText(TargetDate);
        refreshList();

    }

    public void refreshList(){
        ListView eventListView = findViewById(R.id.daycard_events_id);
        EventsAdapter eventsAdapter = new EventsAdapter();
        eventListView.setAdapter(eventsAdapter);
    }

    class EventsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Events != null ? Events.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.day_event_item,null);

            TextView title = view.findViewById(R.id.event_title_id);
            title.setText(Events.get(i).getTitle());
            TextView description = view.findViewById(R.id.event_description_id);
            description.setText(Events.get(i).getDescription());

            TextView startTime = view.findViewById(R.id.start_time_id);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            startTime.setText(dateFormat.format(Events.get(i).getStartTime().getTime()));

            TextView endTime = view.findViewById(R.id.end_time_id);
            endTime.setText(dateFormat.format(Events.get(i).getEndTime().getTime()));

            return view;
        }
    }

    public void createNewTask (View view) {
        Intent intent = new Intent(this, EventFormActivity.class);
        intent.putExtra("currentDate", TargetDate);
        startActivityForResult(intent, POST_EVENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == POST_EVENT_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Event event = data.getParcelableExtra("event");
                Events.add(event);
                refreshList();
            }
        }
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        finish();
    }

}

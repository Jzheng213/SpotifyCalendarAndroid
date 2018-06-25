package com.example.android.spotifycalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class DayActivity extends AppCompatActivity {

    private String TargetDate;
    private ArrayList<Event> Events;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Intent intent = getIntent();
        TargetDate = intent.getStringExtra("date");
        Events = (ArrayList<Event>) intent.getExtras().getSerializable("events");

        tvDate = (TextView) findViewById(R.id.daycard_date_id);
        tvDate.setText(TargetDate);
        ListView eventListView = (ListView) findViewById(R.id.daycard_events_id);
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

            TextView title = (TextView) view.findViewById(R.id.event_title_id);
            title.setText(Events.get(i).getTitle());
            TextView description = (TextView) view.findViewById(R.id.event_description_id);
            description.setText(Events.get(i).getDescription());

            return view;
        }
    }
}

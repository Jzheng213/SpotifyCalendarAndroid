package com.example.android.spotifycalendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
        if (intent.getExtras() != null){
            Events = (ArrayList<Event>) intent.getExtras().getSerializable("events");
        }

        tvDate = findViewById(R.id.daycard_date_id);
        tvDate.setText(TargetDate);
        refreshList();

    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xcc, 0x99,
                    0x00)));
            // set item width
            openItem.setWidth(200);
            // set item title
            openItem.setTitle("Edit");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(250);
            deleteItem.setTitle("Delete");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            // set a icon
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public void refreshList(){
        SwipeMenuListView eventListView = findViewById(R.id.daycard_events_id);
        EventsAdapter eventsAdapter = new EventsAdapter();
        eventListView.setAdapter(eventsAdapter);
        eventListView.setMenuCreator(creator);

        eventListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d("swipe", "onMenuItemClick: clicked item " + index);
                        break;
                    case 1:
                        Log.d("swipe", "onMenuItemClick: clicked item " + index);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

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


}

package com.example.android.spotifycalendar.views;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.utils.APIEventUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DayActivity extends AppCompatActivity {
    private DateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
    private String TargetDate;
    private ArrayList<Event> Events;
    private TextView tvDate;
    final private int POST_EVENT_REQUEST_CODE = 1;
    final private int PATCH_EVENT_REQUEST_CODE = 2;

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

    public void refreshList(){
        ListView eventListView = findViewById(R.id.daycard_events_id);
        EventsAdapter eventsAdapter = new EventsAdapter();
        eventListView.setAdapter(eventsAdapter);
        
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                Event event = Events.get(position);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(DayActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_show_task, null);
                TextView title = mView.findViewById(R.id.show_title_id);
                TextView description = mView.findViewById(R.id.show_description_id);
                TextView start_time = mView.findViewById(R.id.show_start_time_id);
                TextView end_time = mView.findViewById(R.id.show_end_time_id);

                title.setText(event.getTitle());
                description.setText(event.getDescription());
                start_time.setText(String.format("Start: %s", dayFormat.format(event.getStartTime().getTime())));
                end_time.setText(String.format("End: %s", dayFormat.format(event.getEndTime().getTime())));

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

    public void deleteTask(int position) {
        APIEventUtil.deleteEvent(Events.get(position).getID(), this);
        Events.remove(position);
        refreshList();
    }

    public void updateTask (int position) {
        Intent intent = new Intent(this, EventFormActivity.class);
        intent.putExtra("mode", EventFormActivity.PATCH_MODE);
        intent.putExtra("event", Events.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, PATCH_EVENT_REQUEST_CODE);
    }

    public void createNewTask (View view) {
        Intent intent = new Intent(this, EventFormActivity.class);
        intent.putExtra("mode", EventFormActivity.POST_MODE);
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

        if(requestCode == PATCH_EVENT_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Event event = data.getParcelableExtra("event");
                int position = data.getIntExtra("position", -1);
                if(position != -1){
                    Events.remove(position);
                    Events.add(event);
                }
                refreshList();
            }
        }
    }


}

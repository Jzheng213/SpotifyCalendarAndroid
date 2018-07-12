package com.example.android.spotifycalendar.views.Daily;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.spotifycalendar.R;
import com.example.android.spotifycalendar.models.Event;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventsAdapter extends BaseAdapter {
    
    private List<Event> events;
    private Context mContext;
    
    public EventsAdapter(Context mContext, List<Event> events){
        this.events = events;
        this.mContext = mContext;
    }
    
    @Override
    public int getCount() {
        return events != null ? events.size() : 0;
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
        view = LayoutInflater.from(mContext).inflate(R.layout.day_event_item,null);

        TextView title = view.findViewById(R.id.event_title_id);
        title.setText(events.get(i).getTitle());
        TextView description = view.findViewById(R.id.event_description_id);
        description.setText(events.get(i).getDescription());

        TextView startTime = view.findViewById(R.id.start_time_id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        startTime.setText(dateFormat.format(events.get(i).getStartTime().getTime()));

        TextView endTime = view.findViewById(R.id.end_time_id);
        endTime.setText(dateFormat.format(events.get(i).getEndTime().getTime()));

        return view;
    }
}
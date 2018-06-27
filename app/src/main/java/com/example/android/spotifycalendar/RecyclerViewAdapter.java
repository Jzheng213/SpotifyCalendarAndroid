package com.example.android.spotifycalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.spotifycalendar.models.Day;
import com.example.android.spotifycalendar.models.Event;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DayHolder> {
    private Context mContext;
    private List<Day> mData;

    public RecyclerViewAdapter(Context mContext, List<Day> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.monthly_day_view,parent,false);
        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(DayHolder holder, final int position) {
        if(mData.get(position) != null){
            holder.tv_date.setText(String.format("%d", mData.get(position).getDayOfMonth()));
            ArrayList<TextView> tv_events = new ArrayList<>();
            tv_events.add(holder.tv_event1);
            tv_events.add(holder.tv_event2);
            tv_events.add(holder.tv_event3);
            ArrayList<Event> events = mData.get(position).getEvents();

            if(events.size() > 0) {
                for (int i = 0; i < Math.min(events.size(), tv_events.size()); i++) {
                    tv_events.get(i).setText(events.get(i).getTitle());
                    tv_events.get(i).setBackgroundColor(Color.parseColor("#c6ecff"));
                }

                if (events.size() > 3) {
                    holder.tv_eventExtra.setText("...");
                    holder.tv_count.setText(String.format("+ %d", events.size()));
                    holder.tv_count.setVisibility(View.VISIBLE);
                }
            }

            holder.dayCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DayActivity.class);
                    intent.putExtra("date", mData.get(position).getFormattedDate());
                    intent.putExtra("events", mData.get(position).getEvents());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.tv_date.setText("");
            holder.tv_event1.setText("");
            holder.tv_event2.setText("");
            holder.tv_event3.setText("");
            holder.tv_eventExtra.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class DayHolder extends RecyclerView.ViewHolder {
        TextView tv_event1, tv_event2, tv_event3, tv_eventExtra;
        TextView tv_date, tv_count;
        CardView dayCard;
        public DayHolder(View dayView){
            super(dayView);
            tv_count = dayView.findViewById(R.id.task_count_id);
            tv_date = dayView.findViewById(R.id.date_number_id);
            tv_event1 = dayView.findViewById(R.id.date_event_1_id);
            tv_event2 = dayView.findViewById(R.id.date_event_2_id);
            tv_event3 = dayView.findViewById(R.id.date_event_3_id);

            tv_eventExtra = dayView.findViewById(R.id.date_event_extra_id);
            dayCard = itemView.findViewById(R.id.daycard_id);
        }
    }
}

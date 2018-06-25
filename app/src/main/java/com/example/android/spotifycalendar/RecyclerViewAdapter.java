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
            ArrayList<Event> events = mData.get(position).getEvents();
            if(events.size() > 0){
                holder.tv_event.setText(mData.get(position).getEvents().get(0).getTitle());
                holder.tv_event.setBackgroundColor(Color.parseColor("#00cc00"));
            }

            holder.dayCard.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(mContext, DayActivity.class);
                    intent.putExtra("date", mData.get(position).getFormattedDate());
                    intent.putExtra("events", mData.get(position).getEvents());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.tv_date.setText("");
            holder.tv_event.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class DayHolder extends RecyclerView.ViewHolder {
        TextView tv_event;
        TextView tv_date;
        CardView dayCard;
        public DayHolder(View dayView){
            super(dayView);
            tv_date = (TextView) dayView.findViewById(R.id.date_number_id);
            tv_event = (TextView) dayView.findViewById(R.id.date_event_id);
            dayCard = (CardView) itemView.findViewById(R.id.daycard_id);
        }
    }
}

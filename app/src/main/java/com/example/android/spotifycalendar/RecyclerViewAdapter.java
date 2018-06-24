package com.example.android.spotifycalendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    public void onBindViewHolder(DayHolder holder, int position) {
        holder.tv_date.setText(String.format("%d", mData.get(position).getDayOfMonth()));
        holder.tv_event.setText(mData.get(position).getEvents()[0]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class DayHolder extends RecyclerView.ViewHolder {
        TextView tv_event;
        TextView tv_date;

        public DayHolder(View dayView){
            super(dayView);
            tv_date = (TextView) dayView.findViewById(R.id.date_number_id);
            tv_event = (TextView) dayView.findViewById(R.id.date_event_id);
        }
    }
}

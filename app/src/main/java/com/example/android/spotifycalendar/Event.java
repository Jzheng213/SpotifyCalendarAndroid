package com.example.android.spotifycalendar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Event implements Parcelable{
    private String title;
    private String description;
    private Calendar startTime;
    private Calendar endTime;

    public Event(String title, String description, Calendar startTime, Calendar endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

// methods needed to make events parcelable, so this class can be passed as a element type of an arrayList
    public Event(Parcel parcel){
        this.title = parcel.readString();
        this.description = parcel.readString();

        long startMilliseconds = parcel.readLong();
        String startTimeZone = parcel.readString();
        this.startTime = new GregorianCalendar(TimeZone.getTimeZone(startTimeZone));
        this.startTime.setTimeInMillis(startMilliseconds);

        long endMilliseconds = parcel.readLong();
        String endTimeZone = parcel.readString();
        this.endTime = new GregorianCalendar(TimeZone.getTimeZone(endTimeZone));
        this.endTime.setTimeInMillis(endMilliseconds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(startTime.getTimeInMillis());
        dest.writeString(startTime.getTimeZone().getID());
        dest.writeLong(endTime.getTimeInMillis());
        dest.writeString(endTime.getTimeZone().getID());
    }

    public static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source){
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size){
            return new Event[size];
        }
    };
// parcelable end

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    private boolean validateSameDay(){
        return  startTime.get(Calendar.YEAR) == endTime.get(Calendar.YEAR) &&
                startTime.get(Calendar.MONTH) == endTime.get(Calendar.MONTH) &&
                startTime.get(Calendar.DAY_OF_MONTH) == endTime.get(Calendar.DAY_OF_MONTH);
    }

    public boolean save(){
        if(validateSameDay()){
            //TODO: save to database
            return true;
        } else {
            //TODO: reject
            return false;
        }
    }
}
